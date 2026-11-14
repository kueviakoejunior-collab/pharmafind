package tg.pharmafind.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "signalements")
public class Signalement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "type_objet", nullable = false)
    private String typeObjet; // PHARMACIE, DISPONIBILITE

    @Column(name = "objet_id", nullable = false)
    private Long objetId;

    @NotBlank
    @Column(name = "motif", nullable = false)
    private String motif;

    @Column(name = "description_minimisee", length = 1000)
    private String descriptionMinimisee;

    @Column(name = "statut_traitement", nullable = false)
    private String statutTraitement = "NOUVEAU"; // NOUVEAU, EN_COURS, TRAITE, REJETE

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    public Signalement() {
    }

    public Signalement(String typeObjet, Long objetId, String motif, String descriptionMinimisee) {
        this.typeObjet = typeObjet;
        this.objetId = objetId;
        this.motif = motif;
        this.descriptionMinimisee = descriptionMinimisee;
        this.statutTraitement = "NOUVEAU";
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

    public String getStatutTraitement() {
        return statutTraitement;
    }

    public void setStatutTraitement(String statutTraitement) {
        this.statutTraitement = statutTraitement;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
}
