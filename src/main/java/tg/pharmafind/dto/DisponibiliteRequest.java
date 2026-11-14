package tg.pharmafind.dto;

import jakarta.validation.constraints.NotNull;
import tg.pharmafind.entity.DisponibiliteStatut;

public class DisponibiliteRequest {

    @NotNull
    private Long medicamentId;

    @NotNull
    private DisponibiliteStatut statut;

    public DisponibiliteRequest() {
    }

    public DisponibiliteRequest(Long medicamentId, DisponibiliteStatut statut) {
        this.medicamentId = medicamentId;
        this.statut = statut;
    }

    public Long getMedicamentId() {
        return medicamentId;
    }

    public void setMedicamentId(Long medicamentId) {
        this.medicamentId = medicamentId;
    }

    public DisponibiliteStatut getStatut() {
        return statut;
    }

    public void setStatut(DisponibiliteStatut statut) {
        this.statut = statut;
    }
}
