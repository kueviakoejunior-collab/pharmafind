package tg.pharmafind.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.pharmafind.entity.Utilisateur;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByIdentifiantConnexion(String identifiantConnexion);
    boolean existsByIdentifiantConnexion(String identifiantConnexion);
}
