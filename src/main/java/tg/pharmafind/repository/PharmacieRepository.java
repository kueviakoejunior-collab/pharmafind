package tg.pharmafind.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tg.pharmafind.entity.Pharmacie;

import java.util.List;

@Repository
public interface PharmacieRepository extends JpaRepository<Pharmacie, Long> {

    List<Pharmacie> findByStatutValidation(String statutValidation);

    @Query("SELECT p FROM Pharmacie p WHERE p.statutValidation = 'VALIDEE' AND LOWER(p.quartier) LIKE LOWER(CONCAT('%', :quartier, '%'))")
    List<Pharmacie> findValideesByQuartier(@Param("quartier") String quartier);

    @Query("SELECT p FROM Pharmacie p WHERE p.statutValidation = 'VALIDEE' AND (LOWER(p.nom) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(p.adresse) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(p.quartier) LIKE LOWER(CONCAT('%', :q, '%')))")
    List<Pharmacie> searchValidees(@Param("q") String q);
}
