package tg.pharmafind.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tg.pharmafind.entity.*;
import tg.pharmafind.repository.*;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UtilisateurRepository utilisateurRepository;
    private final PharmacieRepository pharmacieRepository;
    private final MedicamentRepository medicamentRepository;
    private final DisponibiliteRepository disponibiliteRepository;
    private final PharmacieUtilisateurRepository pharmacieUtilisateurRepository;
    private final GardeRepository gardeRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UtilisateurRepository utilisateurRepository,
                           PharmacieRepository pharmacieRepository,
                           MedicamentRepository medicamentRepository,
                           DisponibiliteRepository disponibiliteRepository,
                           PharmacieUtilisateurRepository pharmacieUtilisateurRepository,
                           GardeRepository gardeRepository,
                           PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.pharmacieRepository = pharmacieRepository;
        this.medicamentRepository = medicamentRepository;
        this.disponibiliteRepository = disponibiliteRepository;
        this.pharmacieUtilisateurRepository = pharmacieUtilisateurRepository;
        this.gardeRepository = gardeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (utilisateurRepository.count() > 0) {
            log.info("Base de données PharmaFind déjà initialisée.");
            return;
        }

        log.info("Initialisation du jeu de données pilote pour Lomé (Togo)...");

        // 1. Utilisateurs
        Utilisateur admin = new Utilisateur("Administrateur ADN", "admin@pharmafind.tg",
                passwordEncoder.encode("Admin123!"), Role.ROLE_ADMIN);
        Utilisateur proGolfe = new Utilisateur("Dr. Kueviakoe (Pharma Golfe)", "pharmacien.golfe@pharmafind.tg",
                passwordEncoder.encode("Pharmacie123!"), Role.ROLE_PHARMACIE);
        Utilisateur proBe = new Utilisateur("Dr. Mensah (Pharma Bé)", "pharmacien.be@pharmafind.tg",
                passwordEncoder.encode("Pharmacie123!"), Role.ROLE_PHARMACIE);

        utilisateurRepository.saveAll(List.of(admin, proGolfe, proBe));

        // 2. Pharmacies de Lomé
        Pharmacie pGolfe = new Pharmacie("Pharmacie du Golfe", "Boulevard du 13 Janvier / Tokoin", "Tokoin", "+228 22 21 05 44", 6.1368, 1.2185, "07h30 - 22h00");
        Pharmacie pBe = new Pharmacie("Pharmacie de Bé", "Rue du Grand Marché de Bé", "Bè", "+228 22 21 34 50", 6.1282, 1.2361, "08h00 - 21h30");
        Pharmacie pNouvelle = new Pharmacie("Pharmacie Nouvelle", "Boulevard Circulaire, angle Rue de l'OCAM", "Nyékonakpoé", "+228 22 21 12 88", 6.1290, 1.2050, "07h00 - 22h30");
        Pharmacie pAeroport = new Pharmacie("Pharmacie de l'Aéroport", "Face Camp RCGP, Route de l'Aéroport", "Tokoin Trésor", "+228 22 26 10 00", 6.1654, 1.2541, "24h / 24");
        Pharmacie pNation = new Pharmacie("Pharmacie Nation", "Grand contournement, Route de Kpalimé", "Adidogomé", "+228 22 50 15 20", 6.1550, 1.1680, "08h00 - 21h00");
        Pharmacie pAgoe = new Pharmacie("Pharmacie Agoè-Nyivé", "Carrefour 2 Lions, Agoè Assiyéyé", "Agoè", "+228 22 25 78 90", 6.1950, 1.1920, "24h / 24");
        Pharmacie pSolidarite = new Pharmacie("Pharmacie Solidarité", "Avenue Nicolas Grunitzky", "Kodjoviakopé", "+228 22 21 00 12", 6.1210, 1.2110, "07h30 - 21h30");

        pharmacieRepository.saveAll(List.of(pGolfe, pBe, pNouvelle, pAeroport, pNation, pAgoe, pSolidarite));

        // 3. Rattachements utilisateurs - pharmacies
        pharmacieUtilisateurRepository.save(new PharmacieUtilisateur(pGolfe, proGolfe, "PHARMACIEN_TITULAIRE"));
        pharmacieUtilisateurRepository.save(new PharmacieUtilisateur(pBe, proBe, "PHARMACIEN_TITULAIRE"));

        // 4. Médicaments essentiels et courants au Togo
        Medicament mParacetamol500 = new Medicament("Paracétamol", "Paracétamol", "Comprimé", "500 mg", "MED-TG-001");
        Medicament mParacetamol1000 = new Medicament("Doliprane", "Paracétamol", "Comprimé effervescent", "1000 mg", "MED-TG-002");
        Medicament mCoartem = new Medicament("Coartem", "Artéméther / Luméfantrine", "Comprimé", "20 mg / 120 mg", "MED-TG-003");
        Medicament mAmox500 = new Medicament("Amoxicilline", "Amoxicilline", "Gélule", "500 mg", "MED-TG-004");
        Medicament mAugmentin = new Medicament("Augmentin", "Amoxicilline / Acide Clavulanique", "Poudre suspension buvable", "500 mg / 62.5 mg", "MED-TG-005");
        Medicament mIbup400 = new Medicament("Ibuprofène", "Ibuprofène", "Comprimé pelliculé", "400 mg", "MED-TG-006");
        Medicament mSerumPhy = new Medicament("Sérum Physiologique 0.9%", "Chlorure de Sodium", "Flacon pour perfusion", "500 ml", "MED-TG-007");
        Medicament mOmeprazole = new Medicament("Oméprazole", "Oméprazole", "Gélule gastro-résistante", "20 mg", "MED-TG-008");
        Medicament mCipro500 = new Medicament("Ciprofloxacine", "Ciprofloxacine", "Comprimé pelliculé", "500 mg", "MED-TG-009");
        Medicament mSpasfon = new Medicament("Spasfon", "Phloroglucinol / Triméthylphloroglucinol", "Comprimé", "80 mg", "MED-TG-010");
        Medicament mSro = new Medicament("Sels de Réhydratation Orale (SRO)", "Glucose / Chlorure de sodium / Citrate", "Sachet poudre", "20.5 g", "MED-TG-011");

        medicamentRepository.saveAll(List.of(
                mParacetamol500, mParacetamol1000, mCoartem, mAmox500, mAugmentin,
                mIbup400, mSerumPhy, mOmeprazole, mCipro500, mSpasfon, mSro
        ));

        // 5. Gardes actives à Lomé
        LocalDateTime now = LocalDateTime.now();
        Garde gardeGolfe = new Garde(pGolfe, "Ordre National des Pharmaciens du Togo", now.minusDays(1), now.plusDays(6));
        Garde gardeAgoe = new Garde(pAgoe, "Ordre National des Pharmaciens du Togo", now.minusDays(1), now.plusDays(6));
        gardeRepository.saveAll(List.of(gardeGolfe, gardeAgoe));

        // 6. Déclarations initiales de disponibilité
        // Disponibilités confirmées récemment
        disponibiliteRepository.save(new Disponibilite(pGolfe, mParacetamol500, DisponibiliteStatut.CONFIRMEE_RECEMMENT, "Dr. Kueviakoe", 48));
        disponibiliteRepository.save(new Disponibilite(pGolfe, mCoartem, DisponibiliteStatut.CONFIRMEE_RECEMMENT, "Dr. Kueviakoe", 48));
        disponibiliteRepository.save(new Disponibilite(pGolfe, mAmox500, DisponibiliteStatut.CONFIRMEE_RECEMMENT, "Dr. Kueviakoe", 48));
        disponibiliteRepository.save(new Disponibilite(pGolfe, mSerumPhy, DisponibiliteStatut.CONFIRMEE_RECEMMENT, "Dr. Kueviakoe", 48));

        disponibiliteRepository.save(new Disponibilite(pBe, mParacetamol500, DisponibiliteStatut.CONFIRMEE_RECEMMENT, "Dr. Mensah", 48));
        disponibiliteRepository.save(new Disponibilite(pBe, mCoartem, DisponibiliteStatut.CONFIRMEE_RECEMMENT, "Dr. Mensah", 48));
        disponibiliteRepository.save(new Disponibilite(pBe, mAmox500, DisponibiliteStatut.NON_DISPONIBLE, "Dr. Mensah", 48)); // Rupture signalée

        disponibiliteRepository.save(new Disponibilite(pNouvelle, mParacetamol1000, DisponibiliteStatut.CONFIRMEE_RECEMMENT, "Système", 48));
        disponibiliteRepository.save(new Disponibilite(pNation, mCoartem, DisponibiliteStatut.CONFIRMEE_RECEMMENT, "Système", 48));
        disponibiliteRepository.save(new Disponibilite(pAgoe, mCoartem, DisponibiliteStatut.CONFIRMEE_RECEMMENT, "Système", 48));
        disponibiliteRepository.save(new Disponibilite(pAgoe, mParacetamol500, DisponibiliteStatut.CONFIRMEE_RECEMMENT, "Système", 48));

        // Exemple d'un médicament ancien pour tester le statut "À confirmer"
        Disponibilite ancienneDispo = new Disponibilite(pSolidarite, mSerumPhy, DisponibiliteStatut.CONFIRMEE_RECEMMENT, "Dr. Ancien", 48);
        ancienneDispo.setDateConfirmation(now.minusDays(5));
        ancienneDispo.setDateExpirationInformation(now.minusDays(3)); // Expiré -> bascule automatique en A_CONFIRMER
        disponibiliteRepository.save(ancienneDispo);

        log.info("Données pilotes Lomé initialisées avec succès ! (7 pharmacies, 11 médicaments, gardes actives et comptes de test).");
    }
}
