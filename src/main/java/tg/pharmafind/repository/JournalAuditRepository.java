package tg.pharmafind.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.pharmafind.entity.JournalAudit;

import java.util.List;

@Repository
public interface JournalAuditRepository extends JpaRepository<JournalAudit, Long> {
    List<JournalAudit> findAllByOrderByDateActionDesc();
    List<JournalAudit> findByUtilisateurIdOrderByDateActionDesc(Long utilisateurId);
    List<JournalAudit> findByTypeObjetAndObjetIdOrderByDateActionDesc(String typeObjet, Long objetId);
}
