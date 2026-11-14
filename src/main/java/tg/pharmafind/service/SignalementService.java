package tg.pharmafind.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tg.pharmafind.dto.SignalementRequest;
import tg.pharmafind.entity.Signalement;
import tg.pharmafind.repository.SignalementRepository;

import java.util.List;

@Service
public class SignalementService {

    private final SignalementRepository signalementRepository;
    private final AuditService auditService;

    public SignalementService(SignalementRepository signalementRepository, AuditService auditService) {
        this.signalementRepository = signalementRepository;
        this.auditService = auditService;
    }

    @Transactional
    public Signalement createSignalement(SignalementRequest request) {
        Signalement signalement = new Signalement(
                request.getTypeObjet(),
                request.getObjetId(),
                request.getMotif(),
                request.getDescriptionMinimisee()
        );
        return signalementRepository.save(signalement);
    }

    public List<Signalement> getAllSignalements() {
        return signalementRepository.findAllByOrderByDateCreationDesc();
    }

    @Transactional
    public Signalement updateStatut(Long id, String nouveauStatut, Long adminId, String adminNom) {
        Signalement s = signalementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Signalement non trouvé : " + id));
        s.setStatutTraitement(nouveauStatut);
        Signalement saved = signalementRepository.save(s);

        auditService.logAction(adminId, adminNom, "TRAITEMENT_SIGNALEMENT", "SIGNALEMENT", saved.getId(),
                "Passage au statut : " + nouveauStatut);
        return saved;
    }
}
