package tg.pharmafind.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "disponibilites", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"pharmacie_id", "medicament_id"})
})
public class Disponibilite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "pharmacie_id", nullable = false)
    private Pharmacie pharmacie;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "medicament_id", nullable = false)
    private Medicament medicament;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private DisponibiliteStatut statut;

    @Column(name = "date_confirmation", nullable = false)
    private LocalDateTime dateConfirmation;

    @Column(name = "date_expiration_information")
    private LocalDateTime dateExpirationInformation;

    @Column(name = "utilisateur_confirmateur")
    private String utilisateurConfirmateur;

    public Disponibilite() {
    }

    public Disponibilite(Pharmacie pharmacie, Medicament medicament, DisponibiliteStatut statut, String utilisateurConfirmateur, int expirationHours) {
        this.pharmacie = pharmacie;
        this.medicament = medicament;
        this.statut = statut;
        this.utilisateurConfirmateur = utilisateurConfirmateur;
        this.dateConfirmation = LocalDateTime.now();
        this.dateExpirationInformation = this.dateConfirmation.plusHours(expirationHours);
    }

    @PrePersist
    public void prePersist() {
        if (dateConfirmation == null) {
            dateConfirmation = LocalDateTime.now();
        }
        if (dateExpirationInformation == null) {
            dateExpirationInformation = dateConfirmation.plusHours(48);
        }
    }

    /**
     * Calcule le statut effectif : si le statut déclaré était CONFIRMEE_RECEMMENT mais que
     * la date d'expiration est dépassée, retourne A_CONFIRMER conformément à la règle métier 9.2.
     */
    public DisponibiliteStatut getStatutEffectif() {
        if (statut == DisponibiliteStatut.CONFIRMEE_RECEMMENT && dateExpirationInformation != null) {
            if (LocalDateTime.now().isAfter(dateExpirationInformation)) {
                return DisponibiliteStatut.A_CONFIRMER;
            }
        }
        return statut;
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

    public Medicament getMedicament() {
        return medicament;
    }

    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }

    public DisponibiliteStatut getStatut() {
        return statut;
    }

    public void setStatut(DisponibiliteStatut statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateConfirmation() {
        return dateConfirmation;
    }

    public void setDateConfirmation(LocalDateTime dateConfirmation) {
        this.dateConfirmation = dateConfirmation;
    }

    public LocalDateTime getDateExpirationInformation() {
        return dateExpirationInformation;
    }

    public void setDateExpirationInformation(LocalDateTime dateExpirationInformation) {
        this.dateExpirationInformation = dateExpirationInformation;
    }

    public String getUtilisateurConfirmateur() {
        return utilisateurConfirmateur;
    }

    public void setUtilisateurConfirmateur(String utilisateurConfirmateur) {
        this.utilisateurConfirmateur = utilisateurConfirmateur;
    }
}
