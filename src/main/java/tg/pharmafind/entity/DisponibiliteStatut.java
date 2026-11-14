package tg.pharmafind.entity;

public enum DisponibiliteStatut {
    CONFIRMEE_RECEMMENT("Confirmée récemment"),
    A_CONFIRMER("À confirmer"),
    NON_DISPONIBLE("Non disponible"),
    INFORMATION_INDISPONIBLE("Information indisponible");

    private final String libelle;

    DisponibiliteStatut(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
