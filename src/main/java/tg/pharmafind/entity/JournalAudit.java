package tg.pharmafind.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "journal_audit", indexes = {
    @Index(name = "idx_audit_action", columnList = "action"),
    @Index(name = "idx_audit_date", columnList = "date_action")
})
public class JournalAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "utilisateur_id")
    private Long utilisateurId;

    @Column(name = "utilisateur_nom")
    private String utilisateurNom;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "type_objet", nullable = false)
    private String typeObjet;

    @Column(name = "objet_id")
    private Long objetId;

    @Column(name = "date_action", nullable = false)
    private LocalDateTime dateAction;

    @Column(name = "resultat", length = 500)
    private String resultat;

    public JournalAudit() {
    }

    public JournalAudit(Long utilisateurId, String utilisateurNom, String action, String typeObjet, Long objetId, String resultat) {
        this.utilisateurId = utilisateurId;
        this.utilisateurNom = utilisateurNom;
        this.action = action;
        this.typeObjet = typeObjet;
        this.objetId = objetId;
        this.resultat = resultat;
        this.dateAction = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (dateAction == null) {
            dateAction = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public String getUtilisateurNom() {
        return utilisateurNom;
    }

    public void setUtilisateurNom(String utilisateurNom) {
        this.utilisateurNom = utilisateurNom;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    public LocalDateTime getDateAction() {
        return dateAction;
    }

    public void setDateAction(LocalDateTime dateAction) {
        this.dateAction = dateAction;
    }

    public String getResultat() {
        return resultat;
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
    }
}
