package tg.pharmafind.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.pharmafind.entity.PharmacieUtilisateur;

import java.util.List;
import java.util.Optional;

@Repository
public interface PharmacieUtilisateurRepository extends JpaRepository<PharmacieUtilisateur, Long> {
    List<PharmacieUtilisateur> findByUtilisateurId(Long utilisateurId);
    Optional<PharmacieUtilisateur> findByPharmacieIdAndUtilisateurId(Long pharmacieId, Long utilisateurId);
}
