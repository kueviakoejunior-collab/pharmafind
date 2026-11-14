package tg.pharmafind.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tg.pharmafind.entity.JournalAudit;
import tg.pharmafind.repository.JournalAuditRepository;

import java.util.List;

@Service
public class AuditService {

    private final JournalAuditRepository journalAuditRepository;

    public AuditService(JournalAuditRepository journalAuditRepository) {
        this.journalAuditRepository = journalAuditRepository;
    }

    @Transactional
    public JournalAudit logAction(Long utilisateurId, String utilisateurNom, String action, String typeObjet, Long objetId, String resultat) {
        JournalAudit audit = new JournalAudit(utilisateurId, utilisateurNom, action, typeObjet, objetId, resultat);
        return journalAuditRepository.save(audit);
    }

    public List<JournalAudit> getAllLogs() {
        return journalAuditRepository.findAllByOrderByDateActionDesc();
    }

    public List<JournalAudit> getLogsByUser(Long utilisateurId) {
        return journalAuditRepository.findByUtilisateurIdOrderByDateActionDesc(utilisateurId);
    }
}
