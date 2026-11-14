package tg.pharmafind.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "utilisateurs")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nom_affichage", nullable = false)
    private String nomAffichage;

    @NotBlank
    @Email
    @Column(name = "identifiant_connexion", nullable = false, unique = true)
    private String identifiantConnexion;

    @NotBlank
    @Column(name = "mot_de_passe_hash", nullable = false)
    private String motDePasseHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "statut_compte", nullable = false)
    private String statutCompte = "ACTIF";

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    public Utilisateur() {
    }

    public Utilisateur(String nomAffichage, String identifiantConnexion, String motDePasseHash, Role role) {
        this.nomAffichage = nomAffichage;
        this.identifiantConnexion = identifiantConnexion;
        this.motDePasseHash = motDePasseHash;
        this.role = role;
        this.statutCompte = "ACTIF";
        this.dateCreation = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (dateCreation == null) {
            dateCreation = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomAffichage() {
        return nomAffichage;
    }

    public void setNomAffichage(String nomAffichage) {
        this.nomAffichage = nomAffichage;
    }

    public String getIdentifiantConnexion() {
        return identifiantConnexion;
    }

    public void setIdentifiantConnexion(String identifiantConnexion) {
        this.identifiantConnexion = identifiantConnexion;
    }

    public String getMotDePasseHash() {
        return motDePasseHash;
    }

    public void setMotDePasseHash(String motDePasseHash) {
        this.motDePasseHash = motDePasseHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getStatutCompte() {
        return statutCompte;
    }

    public void setStatutCompte(String statutCompte) {
        this.statutCompte = statutCompte;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
}
