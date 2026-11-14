package tg.pharmafind.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tg.pharmafind.dto.AuthRequest;
import tg.pharmafind.dto.AuthResponse;
import tg.pharmafind.entity.PharmacieUtilisateur;
import tg.pharmafind.entity.Utilisateur;
import tg.pharmafind.repository.PharmacieUtilisateurRepository;
import tg.pharmafind.repository.UtilisateurRepository;
import tg.pharmafind.security.JwtTokenProvider;

import java.util.List;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UtilisateurRepository utilisateurRepository;
    private final PharmacieUtilisateurRepository pharmacieUtilisateurRepository;
    private final JwtTokenProvider tokenProvider;
    private final AuditService auditService;

    public AuthService(AuthenticationManager authenticationManager,
                       UtilisateurRepository utilisateurRepository,
                       PharmacieUtilisateurRepository pharmacieUtilisateurRepository,
                       JwtTokenProvider tokenProvider,
                       AuditService auditService) {
        this.authenticationManager = authenticationManager;
        this.utilisateurRepository = utilisateurRepository;
        this.pharmacieUtilisateurRepository = pharmacieUtilisateurRepository;
        this.tokenProvider = tokenProvider;
        this.auditService = auditService;
    }

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getIdentifiant(), request.getMotDePasse())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Utilisateur user = utilisateurRepository.findByIdentifiantConnexion(request.getIdentifiant())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        Long pharmacieId = null;
        String pharmacieNom = null;

        List<PharmacieUtilisateur> affectations = pharmacieUtilisateurRepository.findByUtilisateurId(user.getId());
        if (!affectations.isEmpty()) {
            pharmacieId = affectations.get(0).getPharmacie().getId();
            pharmacieNom = affectations.get(0).getPharmacie().getNom();
        }

        String token = tokenProvider.generateToken(user.getIdentifiantConnexion(), user.getRole().name(), pharmacieId);

        auditService.logAction(user.getId(), user.getNomAffichage(), "CONNEXION", "UTILISATEUR", user.getId(), "Connexion réussie");

        return new AuthResponse(token, user.getId(), user.getNomAffichage(), user.getIdentifiantConnexion(), user.getRole(), pharmacieId, pharmacieNom);
    }

    public Utilisateur getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("Aucun utilisateur connecté");
        }
        return utilisateurRepository.findByIdentifiantConnexion(auth.getName())
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable"));
    }

    public Long getCurrentUserPharmacieId(Utilisateur user) {
        List<PharmacieUtilisateur> affectations = pharmacieUtilisateurRepository.findByUtilisateurId(user.getId());
        if (affectations.isEmpty()) {
            return null;
        }
        return affectations.get(0).getPharmacie().getId();
    }
}
