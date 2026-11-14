package tg.pharmafind.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tg.pharmafind.dto.MedicamentRequest;
import tg.pharmafind.entity.*;
import tg.pharmafind.repository.UtilisateurRepository;
import tg.pharmafind.service.AuditService;
import tg.pharmafind.service.AuthService;
import tg.pharmafind.service.MedicamentService;
import tg.pharmafind.service.PharmacieService;
import tg.pharmafind.service.SignalementService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminApiController {

    private final PharmacieService pharmacieService;
    private final MedicamentService medicamentService;
    private final SignalementService signalementService;
    private final AuditService auditService;
    private final AuthService authService;
    private final UtilisateurRepository utilisateurRepository;

    public AdminApiController(PharmacieService pharmacieService,
                              MedicamentService medicamentService,
                              SignalementService signalementService,
                              AuditService auditService,
                              AuthService authService,
                              UtilisateurRepository utilisateurRepository) {
        this.pharmacieService = pharmacieService;
        this.medicamentService = medicamentService;
        this.signalementService = signalementService;
        this.auditService = auditService;
        this.authService = authService;
        this.utilisateurRepository = utilisateurRepository;
    }

    @GetMapping("/pharmacies")
    public ResponseEntity<List<Pharmacie>> getAllPharmacies() {
        return ResponseEntity.ok(pharmacieService.getAll());
    }

    @PostMapping("/pharmacies/{id}/validation")
    public ResponseEntity<Pharmacie> updatePharmacieValidation(
            @PathVariable Long id,
            @RequestParam("statut") String statut) {
        Utilisateur admin = authService.getCurrentUser();
        return ResponseEntity.ok(pharmacieService.updateValidationStatus(id, statut, admin.getId(), admin.getNomAffichage()));
    }

    @PostMapping("/medicaments")
    public ResponseEntity<Medicament> createMedicament(@Valid @RequestBody MedicamentRequest request) {
        Utilisateur admin = authService.getCurrentUser();
        return ResponseEntity.ok(medicamentService.createMedicament(request, admin.getId(), admin.getNomAffichage()));
    }

    @GetMapping("/signalements")
    public ResponseEntity<List<Signalement>> getSignalements() {
        return ResponseEntity.ok(signalementService.getAllSignalements());
    }

    @PatchMapping("/signalements/{id}/statut")
    public ResponseEntity<Signalement> updateSignalementStatut(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        Utilisateur admin = authService.getCurrentUser();
        String statut = body.get("statut");
        return ResponseEntity.ok(signalementService.updateStatut(id, statut, admin.getId(), admin.getNomAffichage()));
    }

    @GetMapping("/audit")
    public ResponseEntity<List<JournalAudit>> getAuditLogs() {
        return ResponseEntity.ok(auditService.getAllLogs());
    }

    @GetMapping("/comptes")
    public ResponseEntity<List<Utilisateur>> getAllComptes() {
        return ResponseEntity.ok(utilisateurRepository.findAll());
    }
}
