package tg.pharmafind.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.pharmafind.entity.WithdrawalCode;

public interface WithdrawalCodeRepository extends JpaRepository<WithdrawalCode, Long> {
    WithdrawalCode findByPharmacieId(Long pharmacieId);
}
