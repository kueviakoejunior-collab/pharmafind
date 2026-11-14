package tg.pharmafind.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tg.pharmafind.entity.Medicament;

import java.util.List;

@Repository
public interface MedicamentRepository extends JpaRepository<Medicament, Long> {

    List<Medicament> findByStatutValidation(String statutValidation);

    @Query("SELECT m FROM Medicament m WHERE m.statutValidation = 'VALIDE' AND (" +
           "LOWER(m.nomCommercial) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(m.substanceActiveValidee) LIKE LOWER(CONCAT('%', :q, '%')))")
    List<Medicament> searchValides(@Param("q") String q);

    boolean existsByNomCommercialIgnoreCaseAndDosageIgnoreCase(String nomCommercial, String dosage);
}
