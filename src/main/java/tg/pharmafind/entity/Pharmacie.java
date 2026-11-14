package tg.pharmafind.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "pharmacies")
public class Pharmacie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotBlank
    @Column(name = "adresse", nullable = false)
    private String adresse;

    @NotBlank
    @Column(name = "quartier", nullable = false)
    private String quartier;

    @NotBlank
    @Column(name = "telephone", nullable = false)
    private String telephone;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "horaires_texte")
    private String horairesTexte = "07h30 - 21h00 (7j/7)";

    @Column(name = "statut_validation", nullable = false)
    private String statutValidation = "VALIDEE"; // VALIDEE, EN_ATTENTE, SUSPENDUE

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_mise_a_jour")
    private LocalDateTime dateMiseAJour;

    public Pharmacie() {
    }

    public Pharmacie(String nom, String adresse, String quartier, String telephone, Double latitude, Double longitude, String horairesTexte) {
        this.nom = nom;
        this.adresse = adresse;
        this.quartier = quartier;
        this.telephone = telephone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.horairesTexte = horairesTexte;
        this.statutValidation = "VALIDEE";
        this.dateCreation = LocalDateTime.now();
        this.dateMiseAJour = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (dateCreation == null) {
            dateCreation = LocalDateTime.now();
        }
        if (dateMiseAJour == null) {
            dateMiseAJour = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.dateMiseAJour = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getQuartier() {
        return quartier;
    }

    public void setQuartier(String quartier) {
        this.quartier = quartier;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getHorairesTexte() {
        return horairesTexte;
    }

    public void setHorairesTexte(String horairesTexte) {
        this.horairesTexte = horairesTexte;
    }

    public String getStatutValidation() {
        return statutValidation;
    }

    public void setStatutValidation(String statutValidation) {
        this.statutValidation = statutValidation;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateMiseAJour() {
        return dateMiseAJour;
    }

    public void setDateMiseAJour(LocalDateTime dateMiseAJour) {
        this.dateMiseAJour = dateMiseAJour;
    }
}
