package tg.pharmafind.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tg.pharmafind.dto.AuthRequest;
import tg.pharmafind.dto.AuthResponse;
import tg.pharmafind.entity.Utilisateur;
import tg.pharmafind.service.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getProfile() {
        Utilisateur user = authService.getCurrentUser();
        Long pharmacieId = authService.getCurrentUserPharmacieId(user);
        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "nomAffichage", user.getNomAffichage(),
                "identifiant", user.getIdentifiantConnexion(),
                "role", user.getRole(),
                "pharmacieId", pharmacieId != null ? pharmacieId : ""
        ));
    }
}
