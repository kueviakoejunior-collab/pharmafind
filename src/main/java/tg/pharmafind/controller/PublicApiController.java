package tg.pharmafind.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tg.pharmafind.dto.DisponibiliteResponse;
import tg.pharmafind.dto.PharmacieDto;
import tg.pharmafind.dto.SignalementRequest;
import tg.pharmafind.entity.Garde;
import tg.pharmafind.entity.Medicament;
import tg.pharmafind.entity.Signalement;
import tg.pharmafind.repository.GardeRepository;
import tg.pharmafind.service.DisponibiliteService;
import tg.pharmafind.service.MedicamentService;
import tg.pharmafind.service.PharmacieService;
import tg.pharmafind.service.SignalementService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/public")
public class PublicApiController {

    private final MedicamentService medicamentService;
    private final PharmacieService pharmacieService;
    private final DisponibiliteService disponibiliteService;
    private final SignalementService signalementService;
    private final GardeRepository gardeRepository;

    public PublicApiController(MedicamentService medicamentService,
                               PharmacieService pharmacieService,
                               DisponibiliteService disponibiliteService,
                               SignalementService signalementService,
                               GardeRepository gardeRepository) {
        this.medicamentService = medicamentService;
        this.pharmacieService = pharmacieService;
        this.disponibiliteService = disponibiliteService;
        this.signalementService = signalementService;
        this.gardeRepository = gardeRepository;
    }

    @GetMapping("/medicaments")
    public ResponseEntity<List<Medicament>> getAllMedicaments() {
        return ResponseEntity.ok(medicamentService.getAllValides());
    }

    @GetMapping("/medicaments/recherche")
    public ResponseEntity<List<Medicament>> searchMedicaments(@RequestParam(value = "q", required = false) String query) {
        return ResponseEntity.ok(medicamentService.search(query));
    }

    @GetMapping("/medicaments/{id}/disponibilites")
    public ResponseEntity<List<DisponibiliteResponse>> getDisponibilitesByMedicament(@PathVariable Long id) {
        return ResponseEntity.ok(disponibiliteService.getDisponibilitesForMedicament(id));
    }

    @GetMapping("/pharmacies")
    public ResponseEntity<List<PharmacieDto>> getPharmacies(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "quartier", required = false) String quartier,
            @RequestParam(value = "deGarde", required = false) Boolean deGarde) {
        return ResponseEntity.ok(pharmacieService.searchPharmacies(q, quartier, deGarde));
    }

    @GetMapping("/pharmacies/{id}")
    public ResponseEntity<PharmacieDto> getPharmacieDetails(@PathVariable Long id) {
        return ResponseEntity.ok(pharmacieService.getDetailsDto(id));
    }

    @GetMapping("/pharmacies/{id}/disponibilites")
    public ResponseEntity<List<DisponibiliteResponse>> getDisponibilitesByPharmacie(@PathVariable Long id) {
        return ResponseEntity.ok(disponibiliteService.getDisponibilitesByPharmacie(id));
    }

    @GetMapping("/gardes")
    public ResponseEntity<List<Garde>> getActiveGardes() {
        return ResponseEntity.ok(gardeRepository.findCurrentlyActiveGardes(LocalDateTime.now()));
    }

    @PostMapping("/signalements")
    public ResponseEntity<Signalement> createSignalement(@Valid @RequestBody SignalementRequest request) {
        return ResponseEntity.ok(signalementService.createSignalement(request));
    }
}
