package tg.pharmafind.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PharmacieRequest {

    @NotBlank
    private String nom;

    @NotBlank
    private String adresse;

    @NotBlank
    private String quartier;

    @NotBlank
    private String telephone;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    private String horairesTexte;

    public PharmacieRequest() {
    }

    // getters and setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public String getQuartier() { return quartier; }
    public void setQuartier(String quartier) { this.quartier = quartier; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getHorairesTexte() { return horairesTexte; }
    public void setHorairesTexte(String horairesTexte) { this.horairesTexte = horairesTexte; }
}
