package tg.pharmafind.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SignalementRequest {
    @NotBlank
    private String typeObjet; // PHARMACIE ou DISPONIBILITE

    @NotNull
    private Long objetId;

    @NotBlank
    private String motif;

    private String descriptionMinimisee;

    public SignalementRequest() {
    }

    public String getTypeObjet() {
        return typeObjet;
    }

    public void setTypeObjet(String typeObjet) {
        this.typeObjet = typeObjet;
    }

    public Long getObjetId() {
        return objetId;
    }

    public void setObjetId(Long objetId) {
        this.objetId = objetId;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getDescriptionMinimisee() {
        return descriptionMinimisee;
    }

    public void setDescriptionMinimisee(String descriptionMinimisee) {
        this.descriptionMinimisee = descriptionMinimisee;
    }
}
