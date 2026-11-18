package tg.pharmafind.dto;

import jakarta.validation.constraints.NotNull;

public class DisponibiliteUpdateRequest {

    @NotNull
    private Integer quantite;

    @NotNull
    private String statut; // e.g., "EN_STOCK", "EPUISEE"

    public DisponibiliteUpdateRequest() {}

    public DisponibiliteUpdateRequest(Integer quantite, String statut) {
        this.quantite = quantite;
        this.statut = statut;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
