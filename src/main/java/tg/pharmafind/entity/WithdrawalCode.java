package tg.pharmafind.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "withdrawal_codes")
public class WithdrawalCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pharmacie_id", nullable = false, unique = true)
    private Pharmacie pharmacie;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    public WithdrawalCode() {}

    public WithdrawalCode(Pharmacie pharmacie, String code) {
        this.pharmacie = pharmacie;
        this.code = code;
        this.dateCreation = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Pharmacie getPharmacie() { return pharmacie; }
    public void setPharmacie(Pharmacie pharmacie) { this.pharmacie = pharmacie; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}
