package tg.pharmafind;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tg.pharmafind.service.MedicamentService;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MedicamentSearchTest {

    @Test
    @DisplayName("La fonction stripAccents normalise correctement les accents français")
    void testStripAccents() {
        assertEquals("paracetamol", MedicamentService.stripAccents("Paracétamol"));
        assertEquals("be", MedicamentService.stripAccents("Bè"));
        assertEquals("nyekonakpoe", MedicamentService.stripAccents("Nyékonakpoé"));
        assertEquals("omeprazole", MedicamentService.stripAccents("Oméprazole"));
    }
}
