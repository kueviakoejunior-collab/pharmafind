package tg.pharmafind.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tg.pharmafind.dto.DisponibiliteRequest;
import tg.pharmafind.dto.DisponibiliteResponse;
import tg.pharmafind.dto.PharmacieDto;
import tg.pharmafind.entity.JournalAudit;
import tg.pharmafind.entity.Pharmacie;
import tg.pharmafind.entity.Utilisateur;
import tg.pharmafind.service.AuditService;
import tg.pharmafind.service.AuthService;
import tg.pharmafind.service.DisponibiliteService;
import tg.pharmafind.service.PharmacieService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pro")
@PreAuthorize("hasAnyAuthority('ROLE_PHARMACIE', 'ROLE_ADMIN')")
public class ProApiController {

    private final AuthService authService;
    private final PharmacieService pharmacieService;
    private final DisponibiliteService disponibiliteService;
    private final AuditService auditService;

    public ProApiController(AuthService authService,
                            PharmacieService pharmacieService,
                            DisponibiliteService disponibiliteService,
                            AuditService auditService) {
        this.authService = authService;
        this.pharmacieService = pharmacieService;
        this.disponibiliteService = disponibiliteService;
        this.auditService = auditService;
    }

    @GetMapping("/ma-pharmacie")
    public ResponseEntity<PharmacieDto> getMaPharmacie() {
        Utilisateur user = authService.getCurrentUser();
        Long phId = authService.getCurrentUserPharmacieId(user);
        if (phId == null) {
            throw new IllegalStateException("Aucune pharmacie rattachée à cet utilisateur");
        }
        return ResponseEntity.ok(pharmacieService.getDetailsDto(phId));
    }

    @GetMapping("/disponibilites")
    public ResponseEntity<List<DisponibiliteResponse>> getMesDisponibilites() {
        Utilisateur user = authService.getCurrentUser();
        Long phId = authService.getCurrentUserPharmacieId(user);
        if (phId == null) {
            throw new IllegalStateException("Aucune pharmacie rattachée à cet utilisateur");
        }
        return ResponseEntity.ok(disponibiliteService.getDisponibilitesByPharmacie(phId));
    }

    @PostMapping("/disponibilites")
    public ResponseEntity<DisponibiliteResponse> updateDisponibilite(@Valid @RequestBody DisponibiliteRequest request) {
        Utilisateur user = authService.getCurrentUser();
        Long phId = authService.getCurrentUserPharmacieId(user);
        if (phId == null) {
            throw new IllegalStateException("Action non autorisée : compte sans officine rattachée");
        }
        return ResponseEntity.ok(disponibiliteService.declareOrUpdate(phId, request, user.getId(), user.getNomAffichage()));
    }

    @PatchMapping("/pharmacie/horaires")
    public ResponseEntity<Pharmacie> updateHoraires(@RequestBody Map<String, String> body) {
        Utilisateur user = authService.getCurrentUser();
        Long phId = authService.getCurrentUserPharmacieId(user);
        if (phId == null) {
            throw new IllegalStateException("Action non autorisée : compte sans officine rattachée");
        }
        String nouveauxHoraires = body.get("horairesTexte");
        return ResponseEntity.ok(pharmacieService.updateHoraires(phId, nouveauxHoraires, user.getId(), user.getNomAffichage()));
    }

    @GetMapping("/audit")
    public ResponseEntity<List<JournalAudit>> getMesAudits() {
        Utilisateur user = authService.getCurrentUser();
        return ResponseEntity.ok(auditService.getLogsByUser(user.getId()));
    }
}
