package tg.pharmafind.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tg.pharmafind.entity.Disponibilite;
import tg.pharmafind.entity.DisponibiliteStatut;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DisponibiliteRepository extends JpaRepository<Disponibilite, Long> {

    List<Disponibilite> findByPharmacieId(Long pharmacieId);

    List<Disponibilite> findByMedicamentId(Long medicamentId);

    Optional<Disponibilite> findByPharmacieIdAndMedicamentId(Long pharmacieId, Long medicamentId);

    @Query("SELECT d FROM Disponibilite d WHERE d.medicament.id = :medicamentId AND d.pharmacie.statutValidation = 'VALIDEE'")
    List<Disponibilite> findActiveByMedicamentId(@Param("medicamentId") Long medicamentId);

    @Modifying
    @Query("UPDATE Disponibilite d SET d.statut = 'A_CONFIRMER' WHERE d.statut = 'CONFIRMEE_RECEMMENT' AND d.dateExpirationInformation < :now")
    int expireOldDisponibilites(@Param("now") LocalDateTime now);
}
