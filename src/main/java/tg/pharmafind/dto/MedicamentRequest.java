package tg.pharmafind.dto;

import jakarta.validation.constraints.NotBlank;

public class MedicamentRequest {

    @NotBlank
    private String nomCommercial;

    @NotBlank
    private String substanceActiveValidee;

    @NotBlank
    private String forme;

    @NotBlank
    private String dosage;

    private String codeReference;

    public MedicamentRequest() {
    }

    public MedicamentRequest(String nomCommercial, String substanceActiveValidee, String forme, String dosage, String codeReference) {
        this.nomCommercial = nomCommercial;
        this.substanceActiveValidee = substanceActiveValidee;
        this.forme = forme;
        this.dosage = dosage;
        this.codeReference = codeReference;
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

    public String getCodeReference() {
        return codeReference;
    }

    public void setCodeReference(String codeReference) {
        this.codeReference = codeReference;
    }
}
