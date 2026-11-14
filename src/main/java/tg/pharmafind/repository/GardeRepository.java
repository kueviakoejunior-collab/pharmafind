package tg.pharmafind.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tg.pharmafind.entity.Garde;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GardeRepository extends JpaRepository<Garde, Long> {

    List<Garde> findByPharmacieId(Long pharmacieId);

    @Query("SELECT g FROM Garde g WHERE g.statutVerification = 'VERIFIEE' AND :now BETWEEN g.dateDebut AND g.dateFin")
    List<Garde> findCurrentlyActiveGardes(@Param("now") LocalDateTime now);

    @Query("SELECT g FROM Garde g WHERE g.pharmacie.id = :pharmacieId AND g.statutVerification = 'VERIFIEE' AND :now BETWEEN g.dateDebut AND g.dateFin")
    List<Garde> findActiveForPharmacie(@Param("pharmacieId") Long pharmacieId, @Param("now") LocalDateTime now);
}
