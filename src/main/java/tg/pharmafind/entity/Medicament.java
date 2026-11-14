package tg.pharmafind.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "medicaments", indexes = {
    @Index(name = "idx_med_nom", columnList = "nom_commercial"),
    @Index(name = "idx_med_substance", columnList = "substance_active_validee")
})
public class Medicament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nom_commercial", nullable = false)
    private String nomCommercial;

    @NotBlank
    @Column(name = "substance_active_validee", nullable = false)
    private String substanceActiveValidee;

    @NotBlank
    @Column(name = "forme", nullable = false)
    private String forme; // comprimé, sirop, gélule, injectable, collyre...

    @NotBlank
    @Column(name = "dosage", nullable = false)
    private String dosage; // 500mg, 1g, 20mg/120mg...

    @Column(name = "code_reference")
    private String codeReference;

    @Column(name = "statut_validation", nullable = false)
    private String statutValidation = "VALIDE"; // VALIDE, EN_ATTENTE

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    public Medicament() {
    }

    public Medicament(String nomCommercial, String substanceActiveValidee, String forme, String dosage, String codeReference) {
        this.nomCommercial = nomCommercial;
        this.substanceActiveValidee = substanceActiveValidee;
        this.forme = forme;
        this.dosage = dosage;
        this.codeReference = codeReference;
        this.statutValidation = "VALIDE";
        this.dateCreation = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (dateCreation == null) {
            dateCreation = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
