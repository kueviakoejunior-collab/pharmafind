package tg.pharmafind.dto;

import java.time.LocalDateTime;

public class PharmacieDto {
    private Long id;
    private String nom;
    private String adresse;
    private String quartier;
    private String telephone;
    private Double latitude;
    private Double longitude;
    private String horairesTexte;
    private String statutValidation;
    private boolean estDeGarde;
    private LocalDateTime dateMiseAJour;
    private int nombreMedicamentsDisponibles;

    public PharmacieDto() {
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

    public boolean isEstDeGarde() {
        return estDeGarde;
    }

    public void setEstDeGarde(boolean estDeGarde) {
        this.estDeGarde = estDeGarde;
    }

    public LocalDateTime getDateMiseAJour() {
        return dateMiseAJour;
    }

    public void setDateMiseAJour(LocalDateTime dateMiseAJour) {
        this.dateMiseAJour = dateMiseAJour;
    }

    public int getNombreMedicamentsDisponibles() {
        return nombreMedicamentsDisponibles;
    }

    public void setNombreMedicamentsDisponibles(int nombreMedicamentsDisponibles) {
        this.nombreMedicamentsDisponibles = nombreMedicamentsDisponibles;
    }
}
