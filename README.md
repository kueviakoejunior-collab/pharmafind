# PHARMAFIND — Plateforme d'Orientation et d'Information Pharmaceutique

> **Dossier de Conception & Préparation au Lancement**  
> Porteur du projet : **Junior Kueviakoe-Aho**  
> Structure : **ADN — Académie Digitale Numérique, Golfe 1 (Lomé, Togo)**  
> Zone pilote : **Lomé, Togo**

---

## 1. Présentation du Projet

**PharmaFind** est une solution numérique d'information et d'orientation permettant aux résidents et familles de Lomé de :
1. Rechercher en temps réel la disponibilité d'un médicament (par nom commercial ou dénomination commune internationale DCI).
2. Consulter la liste et la cartographie interactive des officines partenaires de Lomé.
3. Vérifier les officines de garde officielles (sources Ordre National des Pharmaciens du Togo et INAM).
4. Accéder aux coordonnées directes (téléphone en 1 clic) pour confirmer le stock avant tout déplacement.
5. Permettre aux professionnels officinaux d'actualiser leurs déclarations de stock en un clic.

### Règle d'or de transparence (Section 1.2 & 9)
> [!IMPORTANT]
> **Déclaration horodatée vs Garantie de stock :**  
> Une disponibilité sur PharmaFind est une déclaration datée de la pharmacie et non une garantie absolue au moment où l'utilisateur se présente au comptoir. La plateforme invite systématiquement à l'appel de confirmation.
> **Expiration automatique (48h) :** Tout statut `Confirmée récemment` non réactualisé après 48h bascule automatiquement en `À confirmer`.

---

## 2. Architecture Technique

- **Backend :** Java 21, Spring Boot 3.3.5 (Spring Data JPA, Spring Security 6 avec JWT, Bean Validation, Scheduled Tasks).
- **Frontend :** HTML5 Responsive Mobile-first, CSS3 Design System médical, JavaScript ES6+, Leaflet & OpenStreetMap.
- **Bases de données :**
  - **Profil `dev` (défaut) :** Base embarquée H2 en mémoire pré-remplie avec le jeu de données pilote de Lomé.
  - **Profil `prod` :** MySQL 8 avec chiffrement, pool HikariCP, scripts de persistance et conteneurisation Docker.
- **Sécurité & Conformité :**
  - Mots de passe hachés avec **BCrypt**.
  - Tokens JWT pour les sessions officines et administrateurs.
  - Conforme aux principes de minimisation de la **Loi togolaise n° 2019-014** (aucun dossier médical ou prescription stockée).

---

## 3. Données Pilotes Pré-configurées (Lomé)

L'application démarre automatiquement avec des données représentatives de Lomé :

### Pharmacies intégrées
- **Pharmacie du Golfe** — Boulevard du 13 Janvier / Tokoin *(De garde)*
- **Pharmacie de Bé** — Rue du Grand Marché de Bé
- **Pharmacie Nouvelle** — Boulevard Circulaire / Nyékonakpoé
- **Pharmacie de l'Aéroport** — Tokoin Trésor
- **Pharmacie Nation** — Adidogomé
- **Pharmacie Agoè-Nyivé** — Carrefour 2 Lions / Assiyéyé *(De garde)*
- **Pharmacie Solidarité** — Kodjoviakopé

### Comptes de démonstration pré-configurés
| Rôle | Identifiant / Email | Mot de passe | Affectation |
| :--- | :--- | :--- | :--- |
| **Administrateur** | `admin@pharmafind.tg` | `Admin123!` | Plateforme entière (Validation, Catalogues, Audits) |
| **Pharmacien Golfe** | `pharmacien.golfe@pharmafind.tg` | `Pharmacie123!` | Pharmacie du Golfe (Tokoin) |
| **Pharmacien Bé** | `pharmacien.be@pharmafind.tg` | `Pharmacie123!` | Pharmacie de Bé |

---

## 4. Démarrage Rapide

### Option A : En un clic sous Windows (Local sans Docker)

1. Double-cliquez sur `build-and-run.bat` (ou lancez `./build-and-run.ps1` dans PowerShell).
2. Ouvrez votre navigateur sur : **`http://localhost:8080`**

### Option B : Lancement en ligne de commande Maven

```powershell
# Compilation et packaging
& "C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2025.2.6.3\plugins\maven\lib\maven3\bin\mvn.cmd" clean package -DskipTests

# Lancement
java -jar target/pharmafind-1.0.0.jar
```

### Option C : Déploiement en Production avec Docker & MySQL

```bash
docker compose up -d --build
```
L'application démarrera automatiquement connectée au conteneur MySQL 8 persistant.

---

## 5. API REST v1

| Méthode | Endpoint | Rôle requis | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/auth/login` | Public | Authentification et obtention du token JWT |
| `GET` | `/api/v1/auth/me` | Authentifié | Profil de l'utilisateur connecté |
| `GET` | `/api/v1/public/medicaments` | Public | Liste des médicaments officiellement référencés |
| `GET` | `/api/v1/public/medicaments/recherche?q=...` | Public | Recherche tolérante par nom commercial ou DCI |
| `GET` | `/api/v1/public/medicaments/{id}/disponibilites` | Public | Disponibilités déclarées pour un médicament à Lomé |
| `GET` | `/api/v1/public/pharmacies` | Public | Annuaire avec filtres quartier et garde |
| `GET` | `/api/v1/public/gardes` | Public | Liste officielle des gardes actives |
| `POST` | `/api/v1/public/signalements` | Public | Dépôt d'un signalement citoyen |
| `GET` | `/api/v1/pro/ma-pharmacie` | Pro / Admin | Consultation de la fiche de son officine |
| `POST` | `/api/v1/pro/disponibilites` | Pro / Admin | Mise à jour en 1 clic de la disponibilité |
| `PATCH` | `/api/v1/pro/pharmacie/horaires` | Pro / Admin | Actualisation des horaires habituels |
| `GET` | `/api/v1/pro/audit` | Pro / Admin | Journal des modifications de l'officine |
| `POST` | `/api/v1/admin/pharmacies/{id}/validation` | Admin | Validation / suspension d'une pharmacie |
| `POST` | `/api/v1/admin/medicaments` | Admin | Ajout d'un nouveau médicament de référence |
| `GET` | `/api/v1/admin/signalements` | Admin | Consultation des retours et anomalies |
| `GET` | `/api/v1/admin/audit` | Admin | Traçabilité globale du système |
