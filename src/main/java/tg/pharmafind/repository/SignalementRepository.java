package tg.pharmafind.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.pharmafind.entity.Signalement;

import java.util.List;

@Repository
public interface SignalementRepository extends JpaRepository<Signalement, Long> {
    List<Signalement> findByStatutTraitement(String statutTraitement);
    List<Signalement> findAllByOrderByDateCreationDesc();
}
