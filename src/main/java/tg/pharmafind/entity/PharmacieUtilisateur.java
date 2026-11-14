package tg.pharmafind.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pharmacie_utilisateurs", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"pharmacie_id", "utilisateur_id"})
})
public class PharmacieUtilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "pharmacie_id", nullable = false)
    private Pharmacie pharmacie;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @Column(name = "role_local", nullable = false)
    private String roleLocal = "PHARMACIEN_TITULAIRE"; // PHARMACIEN_TITULAIRE, ADJOINT, PREPARATEUR

    @Column(name = "statut_affectation", nullable = false)
    private String statutAffectation = "ACTIF";

    public PharmacieUtilisateur() {
    }

    public PharmacieUtilisateur(Pharmacie pharmacie, Utilisateur utilisateur, String roleLocal) {
        this.pharmacie = pharmacie;
        this.utilisateur = utilisateur;
        this.roleLocal = roleLocal;
        this.statutAffectation = "ACTIF";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pharmacie getPharmacie() {
        return pharmacie;
    }

    public void setPharmacie(Pharmacie pharmacie) {
        this.pharmacie = pharmacie;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getRoleLocal() {
        return roleLocal;
    }

    public void setRoleLocal(String roleLocal) {
        this.roleLocal = roleLocal;
    }

    public String getStatutAffectation() {
        return statutAffectation;
    }

    public void setStatutAffectation(String statutAffectation) {
        this.statutAffectation = statutAffectation;
    }
}
