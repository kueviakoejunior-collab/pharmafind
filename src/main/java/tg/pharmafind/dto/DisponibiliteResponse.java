package tg.pharmafind.dto;

import tg.pharmafind.entity.DisponibiliteStatut;
import java.time.LocalDateTime;

public class DisponibiliteResponse {
    private Long id;
    private Long pharmacieId;
    private String pharmacieNom;
    private String pharmacieAdresse;
    private String pharmacieQuartier;
    private String pharmacieTelephone;
    private Double pharmacieLatitude;
    private Double pharmacieLongitude;
    private boolean pharmacieDeGarde;
    private Long medicamentId;
    private String nomCommercial;
    private String substanceActiveValidee;
    private String forme;
    private String dosage;
    private DisponibiliteStatut statut;
    private String statutLibelle;
    private LocalDateTime dateConfirmation;
    private LocalDateTime dateExpirationInformation;
    private String utilisateurConfirmateur;

    public DisponibiliteResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPharmacieId() {
        return pharmacieId;
    }

    public void setPharmacieId(Long pharmacieId) {
        this.pharmacieId = pharmacieId;
    }

    public String getPharmacieNom() {
        return pharmacieNom;
    }

    public void setPharmacieNom(String pharmacieNom) {
        this.pharmacieNom = pharmacieNom;
    }

    public String getPharmacieAdresse() {
        return pharmacieAdresse;
    }

    public void setPharmacieAdresse(String pharmacieAdresse) {
        this.pharmacieAdresse = pharmacieAdresse;
    }

    public String getPharmacieQuartier() {
        return pharmacieQuartier;
    }

    public void setPharmacieQuartier(String pharmacieQuartier) {
        this.pharmacieQuartier = pharmacieQuartier;
    }

    public String getPharmacieTelephone() {
        return pharmacieTelephone;
    }

    public void setPharmacieTelephone(String pharmacieTelephone) {
        this.pharmacieTelephone = pharmacieTelephone;
    }

    public Double getPharmacieLatitude() {
        return pharmacieLatitude;
    }

    public void setPharmacieLatitude(Double pharmacieLatitude) {
        this.pharmacieLatitude = pharmacieLatitude;
    }

    public Double getPharmacieLongitude() {
        return pharmacieLongitude;
    }

    public void setPharmacieLongitude(Double pharmacieLongitude) {
        this.pharmacieLongitude = pharmacieLongitude;
    }

    public boolean isPharmacieDeGarde() {
        return pharmacieDeGarde;
    }

    public void setPharmacieDeGarde(boolean pharmacieDeGarde) {
        this.pharmacieDeGarde = pharmacieDeGarde;
    }

    public Long getMedicamentId() {
        return medicamentId;
    }

    public void setMedicamentId(Long medicamentId) {
        this.medicamentId = medicamentId;
    }

    public String getNomCommercial() {
        return nomCommercial;
    }

    public void setNomCommercial(String nomCommercial) {
        this.nomCommercial = nomCommercial;
    }

    public String getSubstanceActiveValidee() {
        return substanceActiveValidee;
    }

    public void setSubstanceActiveValidee(String substanceActiveValidee) {
        this.substanceActiveValidee = substanceActiveValidee;
    }

    public String getForme() {
        return forme;
    }

    public void setForme(String forme) {
        this.forme = forme;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public DisponibiliteStatut getStatut() {
        return statut;
    }

    public void setStatut(DisponibiliteStatut statut) {
        this.statut = statut;
    }

    public String getStatutLibelle() {
        return statutLibelle;
    }

    public void setStatutLibelle(String statutLibelle) {
        this.statutLibelle = statutLibelle;
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
