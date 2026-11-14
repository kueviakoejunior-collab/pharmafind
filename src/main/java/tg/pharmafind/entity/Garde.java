package tg.pharmafind.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "gardes")
public class Garde {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "pharmacie_id", nullable = false)
    private Pharmacie pharmacie;

    @Column(name = "source", nullable = false)
    private String source = "Ordre National des Pharmaciens du Togo / INAM";

    @Column(name = "date_debut", nullable = false)
    private LocalDateTime dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDateTime dateFin;

    @Column(name = "statut_verification", nullable = false)
    private String statutVerification = "VERIFIEE";

    @Column(name = "date_verification")
    private LocalDateTime dateVerification;

    public Garde() {
    }

    public Garde(Pharmacie pharmacie, String source, LocalDateTime dateDebut, LocalDateTime dateFin) {
        this.pharmacie = pharmacie;
        this.source = source;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statutVerification = "VERIFIEE";
        this.dateVerification = LocalDateTime.now();
    }

    public boolean isCurrentlyActive() {
        LocalDateTime now = LocalDateTime.now();
        return (now.isEqual(dateDebut) || now.isAfter(dateDebut)) && (now.isEqual(dateFin) || now.isBefore(dateFin));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pharmacie getPharmacie() {
        return pharmacie;
    }

    public void setPharmacie(Pharmacie pharmacie) {
        this.pharmacie = pharmacie;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public String getStatutVerification() {
        return statutVerification;
    }

    public void setStatutVerification(String statutVerification) {
        this.statutVerification = statutVerification;
    }

    public LocalDateTime getDateVerification() {
        return dateVerification;
    }

    public void setDateVerification(LocalDateTime dateVerification) {
        this.dateVerification = dateVerification;
    }
}
