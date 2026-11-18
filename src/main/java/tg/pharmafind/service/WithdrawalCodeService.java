package tg.pharmafind.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tg.pharmafind.entity.WithdrawalCode;
import tg.pharmafind.entity.Pharmacie;
import tg.pharmafind.repository.WithdrawalCodeRepository;
import tg.pharmafind.repository.PharmacieRepository;

import java.util.UUID;

@Service
public class WithdrawalCodeService {

    private final WithdrawalCodeRepository withdrawalCodeRepository;
    private final PharmacieRepository pharmacieRepository;
    private final AuditService auditService;

    public WithdrawalCodeService(WithdrawalCodeRepository withdrawalCodeRepository, PharmacieRepository pharmacieRepository, AuditService auditService) {
        this.withdrawalCodeRepository = withdrawalCodeRepository;
        this.pharmacieRepository = pharmacieRepository;
        this.auditService = auditService;
    }

    @Transactional
    public String generateOrGetCode(Long pharmacieId, Long userId, String userNom) {
        Pharmacie pharma = pharmacieRepository.findById(pharmacieId)
                .orElseThrow(() -> new IllegalArgumentException("Pharmacie non trouvée avec l'id : " + pharmacieId));
        WithdrawalCode existing = withdrawalCodeRepository.findByPharmacieId(pharmacieId);
        if (existing != null) {
            return existing.getCode();
        }
        String code = UUID.randomUUID().toString();
        WithdrawalCode wc = new WithdrawalCode(pharma, code);
        withdrawalCodeRepository.save(wc);
        auditService.logAction(userId, userNom, "GENERATION_CODE_RETRAIT", "WITHDRAWAL_CODE", wc.getId(), "Code de retrait généré pour la pharmacie " + pharma.getNom());
        return code;
    }

    public String getCode(Long pharmacieId) {
        WithdrawalCode wc = withdrawalCodeRepository.findByPharmacieId(pharmacieId);
        return wc != null ? wc.getCode() : null;
    }
}
