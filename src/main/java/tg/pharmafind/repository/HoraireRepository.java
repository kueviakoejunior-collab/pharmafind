package tg.pharmafind.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.pharmafind.entity.Horaire;

import java.util.List;

@Repository
public interface HoraireRepository extends JpaRepository<Horaire, Long> {
    List<Horaire> findByPharmacieId(Long pharmacieId);
}
