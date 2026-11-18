# PHARMAHELP — Votre santé, plus accessible (Lomé, Togo)
<!-- redeploy trigger -->

> **Projet porté par Junior Kueviakoe-Aho**  
> Structure : **ADN — Académie Digitale Numérique, Golfe 1 (Lomé, Togo)**  
> Prototype & Design officiel : **[quota-cycle-98469575.figma.site](https://quota-cycle-98469575.figma.site/)**  
> Zone de lancement : **Lomé, Togo**

---

## 1. Présentation & Vision

**PharmaHelp** est une plateforme numérique innovante qui permet à toute personne à Lomé de :
- **Trouver ses médicaments simplement** en consultant les stocks réels et horodatés déclarés par les pharmacies participantes.
- **Scanner son ordonnance médicale** ou boîte de médicament via la caméra du smartphone pour identifier instantanément les produits prescrits (faisceau laser, détection OCR et conformité stricte de protection des données de santé).
- **Préparer son retrait en pharmacie** grâce à un **Code de Retrait unique (`PH-XXXX`)** à présenter au comptoir pour un service express sans attente.
- **Contacter l'officine par WhatsApp ou appel direct** en un clic avec un message pré-rempli pour confirmer la disponibilité avant tout déplacement.
- **Localiser les officines de Lomé** sur une carte interactive Leaflet & OpenStreetMap avec repérage immédiat des **pharmacies de garde 24h/24** (sources officielles Ordre National des Pharmaciens du Togo et INAM).

---

## 2. Fonctionnalités Clés & Expérience Utilisateur

### 📷 Le Scanner d'Ordonnance Intelligent
- Bascule en un clic entre la recherche par saisie textuelle et le scanner d'ordonnance.
- Prise de vue par caméra ou import d'un fichier image.
- Mode de démonstration instantané 1-clic (*« Essayer avec une ordonnance de démonstration »*).
- Animation d'analyse laser avec chiffrement et suppression immédiate après traitement (aucun stockage d'images médicales personnelles).
- Extraction automatique des médicaments (ex: *Doliprane 1000mg*, *Amoxicilline 500mg*, *Coartem*) et bascule directe vers la recherche de disponibilité.

### 🛍️ Préparation de Retrait (Code Unique PH-XXXX)
- Génération d'un code de réservation unique à 6 caractères (ex: `PH-7842`).
- Envoi direct du récapitulatif par **WhatsApp** au pharmacien avec adresse, horaires et médicaments demandés.

### ⏱️ Règle de Fraîcheur & Expiration Automatique (48h)
- Statut 🟢 **Confirmée récemment** : actualisé il y a moins de 48h.
- Statut 🟡 **À confirmer** : bascule automatique au-delà de 48h sans mise à jour, incitant à l'appel avant déplacement.
- Statut 🔴 **Non disponible** : rupture temporaire déclarée.

---

## 3. Données Pilotes de Lomé & Comptes de Test

| Rôle | Identifiant / Email | Mot de passe | Affectation |
| :--- | :--- | :--- | :--- |
| **Administrateur** | `admin@pharmafind.tg` | `Admin123!` | Validation des officines, ajouts catalogue, modération et audits |
| **Pharmacie du Golfe** | `pharmacien.golfe@pharmafind.tg` | `Pharmacie123!` | Boulevard du 13 Janvier / Tokoin (Officine de garde 24h/24) |
| **Pharmacie de Bé** | `pharmacien.be@pharmafind.tg` | `Pharmacie123!` | Rue du Grand Marché de Bé |

---

## 4. Démarrage Rapide

### Option A : Double-clic Windows
Double-cliquez sur `build-and-run.bat` puis rendez-vous sur : **`http://localhost:8080`**.

### Option B : Ligne de commande Java 21
```powershell
java -jar target/pharmafind-1.0.0.jar
```

### Option C : Déploiement Conteneurisé Docker
```bash
docker compose up -d --build
```
