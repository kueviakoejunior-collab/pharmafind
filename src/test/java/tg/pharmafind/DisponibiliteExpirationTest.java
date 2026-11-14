package tg.pharmafind;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tg.pharmafind.entity.Disponibilite;
import tg.pharmafind.entity.DisponibiliteStatut;
import tg.pharmafind.entity.Medicament;
import tg.pharmafind.entity.Pharmacie;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DisponibiliteExpirationTest {

    @Test
    @DisplayName("Règle métier 9.2 : Une disponibilité fraîche doit renvoyer CONFIRMEE_RECEMMENT")
    void testDisponibiliteFraiche() {
        Pharmacie p = new Pharmacie("Pharmacie Test", "Lomé", "Tokoin", "+228 90 00 00 00", 6.13, 1.21, "8h-20h");
        Medicament m = new Medicament("Paracétamol", "Paracétamol", "Comprimé", "500 mg", "REF-01");

        Disponibilite dispo = new Disponibilite(p, m, DisponibiliteStatut.CONFIRMEE_RECEMMENT, "Dr. Test", 48);
        dispo.setDateConfirmation(LocalDateTime.now().minusHours(2));
        dispo.setDateExpirationInformation(LocalDateTime.now().plusHours(46));

        assertEquals(DisponibiliteStatut.CONFIRMEE_RECEMMENT, dispo.getStatutEffectif());
    }

    @Test
    @DisplayName("Règle métier 9.2 : Une disponibilité de plus de 48h doit automatiquement basculer en A_CONFIRMER")
    void testDisponibiliteExpireeDevientAConfirmer() {
        Pharmacie p = new Pharmacie("Pharmacie Test", "Lomé", "Tokoin", "+228 90 00 00 00", 6.13, 1.21, "8h-20h");
        Medicament m = new Medicament("Paracétamol", "Paracétamol", "Comprimé", "500 mg", "REF-01");

        Disponibilite dispo = new Disponibilite(p, m, DisponibiliteStatut.CONFIRMEE_RECEMMENT, "Dr. Test", 48);
        dispo.setDateConfirmation(LocalDateTime.now().minusHours(50));
        dispo.setDateExpirationInformation(LocalDateTime.now().minusHours(2)); // expirée

        assertEquals(DisponibiliteStatut.A_CONFIRMER, dispo.getStatutEffectif());
    }

    @Test
    @DisplayName("Un statut NON_DISPONIBLE ne change pas même si ancien")
    void testStatutNonDisponibleResteInchange() {
        Pharmacie p = new Pharmacie("Pharmacie Test", "Lomé", "Tokoin", "+228 90 00 00 00", 6.13, 1.21, "8h-20h");
        Medicament m = new Medicament("Paracétamol", "Paracétamol", "Comprimé", "500 mg", "REF-01");

        Disponibilite dispo = new Disponibilite(p, m, DisponibiliteStatut.NON_DISPONIBLE, "Dr. Test", 48);
        dispo.setDateConfirmation(LocalDateTime.now().minusHours(60));
        dispo.setDateExpirationInformation(LocalDateTime.now().minusHours(12));

        assertEquals(DisponibiliteStatut.NON_DISPONIBLE, dispo.getStatutEffectif());
    }
}
