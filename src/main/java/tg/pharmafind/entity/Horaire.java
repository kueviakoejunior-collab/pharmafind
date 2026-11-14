package tg.pharmafind.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "horaires")
public class Horaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pharmacie_id", nullable = false)
    private Pharmacie pharmacie;

    @Column(name = "jour", nullable = false)
    private String jour; // Lundi, Mardi, Mercredi, Jeudi, Vendredi, Samedi, Dimanche

    @Column(name = "heure_ouverture", nullable = false)
    private String heureOuverture; // ex: 08:00

    @Column(name = "heure_fermeture", nullable = false)
    private String heureFermeture; // ex: 21:00

    @Column(name = "type_horaire", nullable = false)
    private String typeHoraire = "STANDARD"; // STANDARD, GARDE_NUIT, 24H_24

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    public Horaire() {
    }

    public Horaire(Pharmacie pharmacie, String jour, String heureOuverture, String heureFermeture, String typeHoraire) {
        this.pharmacie = pharmacie;
        this.jour = jour;
        this.heureOuverture = heureOuverture;
        this.heureFermeture = heureFermeture;
        this.typeHoraire = typeHoraire;
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

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public String getHeureOuverture() {
        return heureOuverture;
    }

    public void setHeureOuverture(String heureOuverture) {
        this.heureOuverture = heureOuverture;
    }

    public String getHeureFermeture() {
        return heureFermeture;
    }

    public void setHeureFermeture(String heureFermeture) {
        this.heureFermeture = heureFermeture;
    }

    public String getTypeHoraire() {
        return typeHoraire;
    }

    public void setTypeHoraire(String typeHoraire) {
        this.typeHoraire = typeHoraire;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }
}
