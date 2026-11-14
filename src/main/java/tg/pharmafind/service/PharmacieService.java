package tg.pharmafind.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tg.pharmafind.dto.PharmacieDto;
import tg.pharmafind.entity.DisponibiliteStatut;
import tg.pharmafind.entity.Pharmacie;
import tg.pharmafind.repository.DisponibiliteRepository;
import tg.pharmafind.repository.GardeRepository;
import tg.pharmafind.repository.PharmacieRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PharmacieService {

    private final PharmacieRepository pharmacieRepository;
    private final GardeRepository gardeRepository;
    private final DisponibiliteRepository disponibiliteRepository;
    private final AuditService auditService;

    public PharmacieService(PharmacieRepository pharmacieRepository,
                            GardeRepository gardeRepository,
                            DisponibiliteRepository disponibiliteRepository,
                            AuditService auditService) {
        this.pharmacieRepository = pharmacieRepository;
        this.gardeRepository = gardeRepository;
        this.disponibiliteRepository = disponibiliteRepository;
        this.auditService = auditService;
    }

    public List<PharmacieDto> searchPharmacies(String query, String quartier, Boolean deGardeSeulement) {
        List<Pharmacie> list;
        if (query != null && !query.trim().isEmpty()) {
            list = pharmacieRepository.searchValidees(query.trim());
        } else if (quartier != null && !quartier.trim().isEmpty()) {
            list = pharmacieRepository.findValideesByQuartier(quartier.trim());
        } else {
            list = pharmacieRepository.findByStatutValidation("VALIDEE");
        }

        LocalDateTime now = LocalDateTime.now();

        return list.stream()
                .map(p -> toDto(p, now))
                .filter(dto -> deGardeSeulement == null || !deGardeSeulement || dto.isEstDeGarde())
                .collect(Collectors.toList());
    }

    public List<Pharmacie> getAll() {
        return pharmacieRepository.findAll();
    }

    public Optional<Pharmacie> findById(Long id) {
        return pharmacieRepository.findById(id);
    }

    public PharmacieDto getDetailsDto(Long id) {
        Pharmacie p = pharmacieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pharmacie non trouvée avec l'id : " + id));
        return toDto(p, LocalDateTime.now());
    }

    @Transactional
    public Pharmacie updateValidationStatus(Long pharmacieId, String newStatus, Long adminId, String adminNom) {
        Pharmacie p = pharmacieRepository.findById(pharmacieId)
                .orElseThrow(() -> new IllegalArgumentException("Pharmacie non trouvée avec l'id : " + id(pharmacieId)));
        String ancien = p.getStatutValidation();
        p.setStatutValidation(newStatus);
        p.setDateMiseAJour(LocalDateTime.now());
        Pharmacie saved = pharmacieRepository.save(p);

        auditService.logAction(adminId, adminNom, "MODIFICATION_STATUT_PHARMACIE", "PHARMACIE", saved.getId(),
                "Passage du statut de " + ancien + " à " + newStatus);
        return saved;
    }

    @Transactional
    public Pharmacie updateHoraires(Long pharmacieId, String horairesTexte, Long userId, String userNom) {
        Pharmacie p = pharmacieRepository.findById(pharmacieId)
                .orElseThrow(() -> new IllegalArgumentException("Pharmacie non trouvée avec l'id : " + id(pharmacieId)));
        p.setHorairesTexte(horairesTexte);
        p.setDateMiseAJour(LocalDateTime.now());
        Pharmacie saved = pharmacieRepository.save(p);

        auditService.logAction(userId, userNom, "MODIFICATION_HORAIRES", "PHARMACIE", saved.getId(),
                "Nouveaux horaires : " + horairesTexte);
        return saved;
    }

    public PharmacieDto toDto(Pharmacie p, LocalDateTime now) {
        PharmacieDto dto = new PharmacieDto();
        dto.setId(p.getId());
        dto.setNom(p.getNom());
        dto.setAdresse(p.getAdresse());
        dto.setQuartier(p.getQuartier());
        dto.setTelephone(p.getTelephone());
        dto.setLatitude(p.getLatitude());
        dto.setLongitude(p.getLongitude());
        dto.setHorairesTexte(p.getHorairesTexte());
        dto.setStatutValidation(p.getStatutValidation());
        dto.setDateMiseAJour(p.getDateMiseAJour());

        boolean estGarde = !gardeRepository.findActiveForPharmacie(p.getId(), now).isEmpty();
        dto.setEstDeGarde(estGarde);

        var dispos = disponibiliteRepository.findByPharmacieId(p.getId());
        long dispoCount = dispos.stream()
                .filter(d -> d.getStatutEffectif() == DisponibiliteStatut.CONFIRMEE_RECEMMENT)
                .count();
        dto.setNombreMedicamentsDisponibles((int) dispoCount);

        return dto;
    }

    private Long id(Long id) {
        return id;
    }
}
