package tg.pharmafind.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tg.pharmafind.dto.MedicamentRequest;
import tg.pharmafind.entity.Medicament;
import tg.pharmafind.repository.MedicamentRepository;

import java.text.Normalizer;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicamentService {

    private final MedicamentRepository medicamentRepository;
    private final AuditService auditService;

    public MedicamentService(MedicamentRepository medicamentRepository, AuditService auditService) {
        this.medicamentRepository = medicamentRepository;
        this.auditService = auditService;
    }

    public List<Medicament> getAllValides() {
        return medicamentRepository.findByStatutValidation("VALIDE");
    }

    /**
     * Recherche conforme à la section 7.1 du cahier des charges :
     * - Insensible à la casse et aux accents (ex: "paracetamol" trouve "Paracétamol")
     * - Recherche par nom commercial et substance active (DCI)
     * - Tri des résultats : correspondances exactes ou commençant par la requête d'abord.
     */
    public List<Medicament> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllValides();
        }

        final String cleanQuery = stripAccents(query.trim());
        List<Medicament> all = getAllValides();

        return all.stream()
                .filter(m -> {
                    String nomClean = stripAccents(m.getNomCommercial());
                    String dciClean = stripAccents(m.getSubstanceActiveValidee());
                    return nomClean.contains(cleanQuery) || dciClean.contains(cleanQuery);
                })
                .sorted(Comparator.comparingInt((Medicament m) -> {
                    String nomClean = stripAccents(m.getNomCommercial());
                    if (nomClean.equalsIgnoreCase(cleanQuery)) return 0;
                    if (nomClean.startsWith(cleanQuery)) return 1;
                    return 2;
                }).thenComparing(Medicament::getNomCommercial))
                .collect(Collectors.toList());
    }

    public static String stripAccents(String input) {
        if (input == null) return "";
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT);
    }

    public Optional<Medicament> findById(Long id) {
        return medicamentRepository.findById(id);
    }

    @Transactional
    public Medicament createMedicament(MedicamentRequest request, Long adminId, String adminNom) {
        Medicament medicament = new Medicament(
                request.getNomCommercial().trim(),
                request.getSubstanceActiveValidee().trim(),
                request.getForme().trim(),
                request.getDosage().trim(),
                request.getCodeReference() != null ? request.getCodeReference().trim() : null
        );
        Medicament saved = medicamentRepository.save(medicament);
        auditService.logAction(adminId, adminNom, "CREATION_MEDICAMENT", "MEDICAMENT", saved.getId(),
                "Ajout médicament : " + saved.getNomCommercial() + " (" + saved.getDosage() + ")");
        return saved;
    }
}
