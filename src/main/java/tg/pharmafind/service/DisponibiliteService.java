package tg.pharmafind.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tg.pharmafind.dto.DisponibiliteRequest;
import tg.pharmafind.dto.DisponibiliteResponse;
import tg.pharmafind.entity.Disponibilite;
import tg.pharmafind.entity.DisponibiliteStatut;
import tg.pharmafind.entity.Medicament;
import tg.pharmafind.entity.Pharmacie;
import tg.pharmafind.repository.DisponibiliteRepository;
import tg.pharmafind.repository.GardeRepository;
import tg.pharmafind.repository.MedicamentRepository;
import tg.pharmafind.repository.PharmacieRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DisponibiliteService {

    private static final Logger log = LoggerFactory.getLogger(DisponibiliteService.class);

    private final DisponibiliteRepository disponibiliteRepository;
    private final PharmacieRepository pharmacieRepository;
    private final MedicamentRepository medicamentRepository;
    private final GardeRepository gardeRepository;
    private final AuditService auditService;

    @Value("${pharmafind.availability.expiry-hours:48}")
    private int expiryHours;

    public DisponibiliteService(DisponibiliteRepository disponibiliteRepository,
                                PharmacieRepository pharmacieRepository,
                                MedicamentRepository medicamentRepository,
                                GardeRepository gardeRepository,
                                AuditService auditService) {
        this.disponibiliteRepository = disponibiliteRepository;
        this.pharmacieRepository = pharmacieRepository;
        this.medicamentRepository = medicamentRepository;
        this.gardeRepository = gardeRepository;
        this.auditService = auditService;
    }

    public List<DisponibiliteResponse> getDisponibilitesForMedicament(Long medicamentId) {
        LocalDateTime now = LocalDateTime.now();
        List<Disponibilite> list = disponibiliteRepository.findActiveByMedicamentId(medicamentId);
        return list.stream().map(d -> toResponse(d, now)).collect(Collectors.toList());
    }

    public List<DisponibiliteResponse> getDisponibilitesByPharmacie(Long pharmacieId) {
        LocalDateTime now = LocalDateTime.now();
        List<Disponibilite> list = disponibiliteRepository.findByPharmacieId(pharmacieId);
        return list.stream().map(d -> toResponse(d, now)).collect(Collectors.toList());
    }

    @Transactional
    public DisponibiliteResponse declareOrUpdate(Long pharmacieId, DisponibiliteRequest request, Long userId, String userNom) {
        Pharmacie pharmacie = pharmacieRepository.findById(pharmacieId)
                .orElseThrow(() -> new IllegalArgumentException("Pharmacie non trouvée : " + pharmacieId));
        Medicament medicament = medicamentRepository.findById(request.getMedicamentId())
                .orElseThrow(() -> new IllegalArgumentException("Médicament non trouvé : " + request.getMedicamentId()));

        Optional<Disponibilite> existingOpt = disponibiliteRepository.findByPharmacieIdAndMedicamentId(pharmacieId, request.getMedicamentId());

        Disponibilite dispo;
        String action;
        if (existingOpt.isPresent()) {
            dispo = existingOpt.get();
            dispo.setStatut(request.getStatut());
            dispo.setDateConfirmation(LocalDateTime.now());
            dispo.setDateExpirationInformation(LocalDateTime.now().plusHours(expiryHours));
            dispo.setUtilisateurConfirmateur(userNom);
            action = "MISE_A_JOUR_DISPONIBILITE";
        } else {
            dispo = new Disponibilite(pharmacie, medicament, request.getStatut(), userNom, expiryHours);
            action = "CREATION_DISPONIBILITE";
        }

        Disponibilite saved = disponibiliteRepository.save(dispo);

        auditService.logAction(userId, userNom, action, "DISPONIBILITE", saved.getId(),
                "Déclaration : " + medicament.getNomCommercial() + " -> " + saved.getStatut().getLibelle() + " à " + pharmacie.getNom());

        return toResponse(saved, LocalDateTime.now());

    @Transactional
    public DisponibiliteResponse adminUpdateStock(Long disponibiliteId, DisponibiliteUpdateRequest request, Long adminId, String adminNom) {
        Disponibilite dispo = disponibiliteRepository.findById(disponibiliteId)
                .orElseThrow(() -> new IllegalArgumentException("Disponibilité non trouvée : " + disponibiliteId));
        dispo.setStatut(request.getStatut());
        // Save the updated availability
        Disponibilite saved = disponibiliteRepository.save(dispo);
        auditService.logAction(adminId, adminNom, "MISE_A_JOUR_STOCK", "DISPONIBILITE", saved.getId(),
                "Mise à jour du statut via admin : " + request.getStatut());
        return toResponse(saved, LocalDateTime.now());
    }
    

    /**
     * Tâche planifiée exécutée toutes les heures : bascule automatiquement les statuts
     * CONFIRMEE_RECEMMENT expirés (> 48h) en A_CONFIRMER selon la règle métier 9.2.
     */
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void runAutomaticExpirationCheck() {
        LocalDateTime now = LocalDateTime.now();
        int updated = disponibiliteRepository.expireOldDisponibilites(now);
        if (updated > 0) {
            log.info("Règle métier 9.2 appliquée : {} disponibilités expirées basculées en 'A_CONFIRMER'", updated);
        }
    }

    public DisponibiliteResponse toResponse(Disponibilite d, LocalDateTime now) {
        DisponibiliteResponse res = new DisponibiliteResponse();
        res.setId(d.getId());

        Pharmacie p = d.getPharmacie();
        res.setPharmacieId(p.getId());
        res.setPharmacieNom(p.getNom());
        res.setPharmacieAdresse(p.getAdresse());
        res.setPharmacieQuartier(p.getQuartier());
        res.setPharmacieTelephone(p.getTelephone());
        res.setPharmacieLatitude(p.getLatitude());
        res.setPharmacieLongitude(p.getLongitude());

        boolean estGarde = !gardeRepository.findActiveForPharmacie(p.getId(), now).isEmpty();
        res.setPharmacieDeGarde(estGarde);

        Medicament m = d.getMedicament();
        res.setMedicamentId(m.getId());
        res.setNomCommercial(m.getNomCommercial());
        res.setSubstanceActiveValidee(m.getSubstanceActiveValidee());
        res.setForme(m.getForme());
        res.setDosage(m.getDosage());

        // Calcul dynamique de l'expiration
        DisponibiliteStatut effectif = d.getStatutEffectif();
        res.setStatut(effectif);
        res.setStatutLibelle(effectif.getLibelle());

        res.setDateConfirmation(d.getDateConfirmation());
        res.setDateExpirationInformation(d.getDateExpirationInformation());
        res.setUtilisateurConfirmateur(d.getUtilisateurConfirmateur());

        return res;
    }
}
