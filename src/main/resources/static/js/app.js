/**
 * PHARMAHELP — APPLICATION JAVASCRIPT FRONTEND
 * Inspiré de la maquette Figma officielle (https://quota-cycle-98469575.figma.site/)
 * Porté par Junior Kueviakoe-Aho · ADN Golfe 1 (Lomé, Togo)
 */

const API_BASE = '/api/v1';

const state = {
    currentUser: null,
    token: localStorage.getItem('pharmahelp_token') || null,
    currentTab: 'search',
    searchMode: 'text', // 'text' ou 'scan'
    allMedicaments: [],
    selectedMedicament: null,
    detectedPrescriptionMedicines: [],
    pharmaciesList: [],
    disponibilitesList: [],
    map: null,
    markersLayer: null,
    selectedPharmacieForReport: null,
    activeWithdrawal: null
};

// ============================================================================
// DONNÉES PILOTES DE SECOURS (LOMÉ) POUR DÉPLOIEMENT STATIQUE / VERCEL
// ============================================================================
const FALLBACK_MEDS = [
    { id: 1, nomCommercial: "Paracétamol", substanceActiveValidee: "Paracétamol", forme: "Comprimé", dosage: "500 mg" },
    { id: 2, nomCommercial: "Doliprane", substanceActiveValidee: "Paracétamol", forme: "Comprimé effervescent", dosage: "1000 mg" },
    { id: 3, nomCommercial: "Coartem", substanceActiveValidee: "Artéméther / Luméfantrine", forme: "Comprimé", dosage: "20 mg / 120 mg" },
    { id: 4, nomCommercial: "Amoxicilline", substanceActiveValidee: "Amoxicilline", forme: "Gélule", dosage: "500 mg" },
    { id: 5, nomCommercial: "Sérum Physiologique", substanceActiveValidee: "Chlorure de Sodium", forme: "Flacon perfusion", dosage: "500 ml" },
    { id: 6, nomCommercial: "Ibuprofène", substanceActiveValidee: "Ibuprofène", forme: "Comprimé pelliculé", dosage: "400 mg" },
    { id: 7, nomCommercial: "Oméprazole", substanceActiveValidee: "Oméprazole", forme: "Gélule gastro-résistante", dosage: "20 mg" }
];

const FALLBACK_DISPOS = [
    {
        id: 101, pharmacieId: 1, pharmacieNom: "Pharmacie du Golfe", pharmacieAdresse: "Boulevard du 13 Janvier / Tokoin",
        pharmacieQuartier: "Tokoin", pharmacieTelephone: "+228 22 21 05 44", pharmacieLatitude: 6.1368, pharmacieLongitude: 1.2185,
        pharmacieDeGarde: true, medicamentId: 1, nomCommercial: "Paracétamol", substanceActiveValidee: "Paracétamol",
        forme: "Comprimé", dosage: "500 mg", statut: "CONFIRMEE_RECEMMENT", statutLibelle: "Confirmée récemment",
        dateConfirmation: new Date().toISOString(), utilisateurConfirmateur: "Dr. Kueviakoe"
    },
    {
        id: 102, pharmacieId: 2, pharmacieNom: "Pharmacie de Bé", pharmacieAdresse: "Rue du Grand Marché de Bé",
        pharmacieQuartier: "Bè", pharmacieTelephone: "+228 22 21 34 50", pharmacieLatitude: 6.1282, pharmacieLongitude: 1.2361,
        pharmacieDeGarde: false, medicamentId: 1, nomCommercial: "Paracétamol", substanceActiveValidee: "Paracétamol",
        forme: "Comprimé", dosage: "500 mg", statut: "CONFIRMEE_RECEMMENT", statutLibelle: "Confirmée récemment",
        dateConfirmation: new Date().toISOString(), utilisateurConfirmateur: "Dr. Mensah"
    },
    {
        id: 103, pharmacieId: 6, pharmacieNom: "Pharmacie Agoè-Nyivé", pharmacieAdresse: "Carrefour 2 Lions, Agoè Assiyéyé",
        pharmacieQuartier: "Agoè", pharmacieTelephone: "+228 22 25 78 90", pharmacieLatitude: 6.1950, pharmacieLongitude: 1.1920,
        pharmacieDeGarde: true, medicamentId: 1, nomCommercial: "Paracétamol", substanceActiveValidee: "Paracétamol",
        forme: "Comprimé", dosage: "500 mg", statut: "CONFIRMEE_RECEMMENT", statutLibelle: "Confirmée récemment",
        dateConfirmation: new Date().toISOString(), utilisateurConfirmateur: "Responsable"
    },
    {
        id: 104, pharmacieId: 1, pharmacieNom: "Pharmacie du Golfe", pharmacieAdresse: "Boulevard du 13 Janvier / Tokoin",
        pharmacieQuartier: "Tokoin", pharmacieTelephone: "+228 22 21 05 44", pharmacieLatitude: 6.1368, pharmacieLongitude: 1.2185,
        pharmacieDeGarde: true, medicamentId: 3, nomCommercial: "Coartem", substanceActiveValidee: "Artéméther / Luméfantrine",
        forme: "Comprimé", dosage: "20 mg / 120 mg", statut: "CONFIRMEE_RECEMMENT", statutLibelle: "Confirmée récemment",
        dateConfirmation: new Date().toISOString(), utilisateurConfirmateur: "Dr. Kueviakoe"
    },
    {
        id: 105, pharmacieId: 2, pharmacieNom: "Pharmacie de Bé", pharmacieAdresse: "Rue du Grand Marché de Bé",
        pharmacieQuartier: "Bè", pharmacieTelephone: "+228 22 21 34 50", pharmacieLatitude: 6.1282, pharmacieLongitude: 1.2361,
        pharmacieDeGarde: false, medicamentId: 4, nomCommercial: "Amoxicilline", substanceActiveValidee: "Amoxicilline",
        forme: "Gélule", dosage: "500 mg", statut: "NON_DISPONIBLE", statutLibelle: "Non disponible",
        dateConfirmation: new Date().toISOString(), utilisateurConfirmateur: "Dr. Mensah"
    }
];

// ============================================================================
// INITIALISATION
// ============================================================================
document.addEventListener('DOMContentLoaded', async () => {
    initNavigation();
    initMap();
    await checkSession();
    await loadInitialMedicaments();
    await searchMedicament('Paracétamol');
});

// ============================================================================
// GESTION DES ONGLETS & MODES
// ============================================================================
function initNavigation() {
    const tabs = document.querySelectorAll('.nav-tab');
    tabs.forEach(tab => {
        tab.addEventListener('click', (e) => {
            const tabName = e.target.getAttribute('data-tab');
            switchTab(tabName);
        });
    });
}

function switchTab(tabName) {
    state.currentTab = tabName;
    document.querySelectorAll('.nav-tab').forEach(t => {
        t.classList.toggle('active', t.getAttribute('data-tab') === tabName);
    });

    document.getElementById('view-search').style.display = (tabName === 'search') ? 'block' : 'none';
    document.getElementById('view-gardes').style.display = (tabName === 'gardes') ? 'block' : 'none';
    document.getElementById('view-pro').style.display = (tabName === 'pro') ? 'block' : 'none';
    document.getElementById('view-admin').style.display = (tabName === 'admin') ? 'block' : 'none';

    if (tabName === 'gardes') {
        loadGardesView();
    } else if (tabName === 'pro') {
        loadProView();
    } else if (tabName === 'admin') {
        loadAdminView();
    } else if (tabName === 'search' && state.map) {
        setTimeout(() => state.map.invalidateSize(), 200);
    }
}

function setSearchMode(mode) {
    state.searchMode = mode;
    document.getElementById('btnModeText').classList.toggle('active', mode === 'text');
    document.getElementById('btnModeScan').classList.toggle('active', mode === 'scan');

    document.getElementById('searchModeTextContainer').style.display = (mode === 'text') ? 'block' : 'none';
    document.getElementById('searchModeScanContainer').style.display = (mode === 'scan') ? 'block' : 'none';
}

function triggerQuickSearch(drugName) {
    document.getElementById('searchQueryInput').value = drugName;
    setSearchMode('text');
    searchMedicament(drugName);
}

// ============================================================================
// SCANNER D'ORDONNANCE (MAQUETTE FIGMA)
// ============================================================================
function runDemoScan() {
    startScanningAnimation(["Doliprane 1000mg", "Amoxicilline 500mg"]);
}

function handlePrescriptionFile(event) {
    const file = event.target.files[0];
    if (!file) return;
    startScanningAnimation(["Doliprane 1000mg", "Amoxicilline 500mg"]);
}

async function startLiveCamera() {
    const video = document.getElementById('cameraVideo');
    const prompt = document.getElementById('scannerIdlePrompt');

    try {
        const stream = await navigator.mediaDevices.getUserMedia({ video: { facingMode: 'environment' } });
        video.srcObject = stream;
        video.style.display = 'block';
        prompt.style.display = 'none';

        // Lancement automatique du scan après 1.5s
        setTimeout(() => {
            startScanningAnimation(["Coartem 20/120mg", "Paracétamol 500mg"]);
        }, 1500);
    } catch (err) {
        alert('Accès caméra non disponible. Vous pouvez utiliser le bouton "Essayer avec une ordonnance de démonstration".');
    }
}

function startScanningAnimation(detectedDrugs) {
    const card = document.getElementById('scannerCardBox');
    const laser = document.getElementById('scannerLaserBeam');
    const detectedBox = document.getElementById('detectedPrescriptionBox');
    const listEl = document.getElementById('detectedMedicinesList');

    card.classList.add('scanning');
    laser.style.display = 'block';
    detectedBox.style.display = 'none';

    setTimeout(() => {
        card.classList.remove('scanning');
        laser.style.display = 'none';

        state.detectedPrescriptionMedicines = detectedDrugs;
        listEl.innerHTML = detectedDrugs.map(d => `<span class="detected-chip">💊 ${d}</span>`).join('');
        detectedBox.style.display = 'block';
    }, 1800);
}

function searchDetectedPrescription() {
    if (state.detectedPrescriptionMedicines.length > 0) {
        const firstMed = state.detectedPrescriptionMedicines[0].split(' ')[0];
        document.getElementById('searchQueryInput').value = firstMed;
        searchMedicament(firstMed);
        // Faire défiler vers les résultats
        document.getElementById('searchResultsContainer').scrollIntoView({ behavior: 'smooth' });
    }
}

// ============================================================================
// CARTOGRAPHIE LEAFLET (LOMÉ, TOGO)
// ============================================================================
function initMap() {
    const mapElement = document.getElementById('lomeMap');
    if (!mapElement) return;

    state.map = L.map('lomeMap').setView([6.1375, 1.2125], 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; OpenStreetMap | PharmaHelp Lomé (Togo)',
        maxZoom: 18
    }).addTo(state.map);

    state.markersLayer = L.layerGroup().addTo(state.map);
}

function updateMapMarkers(pharmacies) {
    if (!state.map || !state.markersLayer) return;

    state.markersLayer.clearLayers();
    const bounds = [];

    pharmacies.forEach(p => {
        if (p.latitude && p.longitude) {
            const isGarde = p.estDeGarde || p.pharmacieDeGarde;
            const markerColor = isGarde ? '#d97706' : '#0e7869';

            const customIcon = L.divIcon({
                className: 'custom-map-pin',
                html: `<div style="background-color: ${markerColor}; width: 30px; height: 30px; border-radius: 50%; border: 3px solid white; box-shadow: 0 4px 10px rgba(0,0,0,0.3); display: flex; align-items: center; justify-content: center; color: white; font-size: 14px; font-weight: 800;">✚</div>`,
                iconSize: [30, 30],
                iconAnchor: [15, 15]
            });

            const marker = L.marker([p.latitude, p.longitude], { icon: customIcon });

            const popupContent = `
                <div style="font-size: 13px; font-family: system-ui;">
                    <strong style="color: #153f38; font-size: 14px;">${p.nom || p.pharmacieNom}</strong><br/>
                    <span>📍 ${p.quartier || p.pharmacieQuartier || ''}</span><br/>
                    <span>📞 <a href="tel:${p.telephone || p.pharmacieTelephone}">${p.telephone || p.pharmacieTelephone}</a></span><br/>
                    ${isGarde ? '<span style="color:#b45309; font-weight:bold;">⚡ De garde 24h/24</span><br/>' : ''}
                    <button onclick="focusCard(${p.id || p.pharmacieId})" style="margin-top:6px; background:#0e7869; color:white; border:none; border-radius:4px; padding:4px 8px; font-weight:bold; cursor:pointer;">Voir la fiche</button>
                </div>
            `;
            marker.bindPopup(popupContent);
            state.markersLayer.addLayer(marker);
            bounds.push([p.latitude, p.longitude]);
        }
    });

    if (bounds.length > 0) {
        state.map.fitBounds(bounds, { padding: [40, 40], maxZoom: 14 });
    }
}

// ============================================================================
// RECHERCHE PUBLIQUE DE MÉDICAMENTS
// ============================================================================
async function loadInitialMedicaments() {
    try {
        const res = await fetch(`${API_BASE}/public/medicaments`);
        if (res.ok) {
            state.allMedicaments = await res.json();
            populateMedicamentDatalist(state.allMedicaments);
            return;
        }
    } catch (e) {
        console.warn('Backend local non détecté, utilisation des données PharmaHelp Lomé embarquées.');
    }
    state.allMedicaments = FALLBACK_MEDS;
    populateMedicamentDatalist(state.allMedicaments);
}

function populateMedicamentDatalist(medicaments) {
    const datalist = document.getElementById('medicamentSuggestions');
    if (!datalist) return;
    datalist.innerHTML = '';
    medicaments.forEach(m => {
        const opt = document.createElement('option');
        opt.value = m.nomCommercial;
        opt.label = `${m.dosage} (${m.forme}) - DCI: ${m.substanceActiveValidee}`;
        datalist.appendChild(opt);
    });
}

async function searchMedicament(query) {
    const q = query !== undefined ? query : document.getElementById('searchQueryInput').value.trim();
    if (!q) return;

    try {
        const res = await fetch(`${API_BASE}/public/medicaments/recherche?q=${encodeURIComponent(q)}`);
        if (res.ok) {
            const results = await res.json();
            if (results && results.length > 0) {
                state.selectedMedicament = results[0];
                await loadDisponibilitesForMedicament(state.selectedMedicament.id);
                return;
            }
        }
    } catch (e) {
        console.warn('Mode hors ligne / statique');
    }

    // Recherche de secours locale
    const cleanQ = (q || '').toLowerCase();
    const localMatch = state.allMedicaments.filter(m => 
        m.nomCommercial.toLowerCase().includes(cleanQ) || 
        m.substanceActiveValidee.toLowerCase().includes(cleanQ)
    );

    if (localMatch.length > 0) {
        state.selectedMedicament = localMatch[0];
        await loadDisponibilitesForMedicament(state.selectedMedicament.id);
    } else {
        renderZeroResults(q);
    }
}

async function loadDisponibilitesForMedicament(medicamentId) {
    try {
        const res = await fetch(`${API_BASE}/public/medicaments/${medicamentId}/disponibilites`);
        if (res.ok) {
            const dispos = await res.json();
            state.disponibilitesList = dispos;
            applyFiltersAndRender();
            return;
        }
    } catch (e) {
        console.warn('Utilisation disponibilités locales');
    }

    // Disponibilités locales de secours
    const localDispos = FALLBACK_DISPOS.filter(d => d.medicamentId === medicamentId);
    state.disponibilitesList = localDispos.length > 0 ? localDispos : FALLBACK_DISPOS.slice(0, 2);
    applyFiltersAndRender();
}

function applyFiltersAndRender() {
    const quartierFilter = document.getElementById('filterQuartier').value;
    const gardeFilter = document.getElementById('filterGardeOnly').checked;

    let filtered = [...state.disponibilitesList];

    if (quartierFilter) {
        filtered = filtered.filter(d => (d.pharmacieQuartier || '').toLowerCase() === quartierFilter.toLowerCase());
    }
    if (gardeFilter) {
        filtered = filtered.filter(d => d.pharmacieDeGarde === true);
    }

    renderDisponibilitesResults(filtered);
    updateMapMarkers(filtered);
}

function renderDisponibilitesResults(dispos) {
    const container = document.getElementById('searchResultsContainer');
    const medHeader = document.getElementById('selectedMedicineHeader');

    if (!state.selectedMedicament) {
        container.innerHTML = '<p class="text-muted">Saisissez ou scannez un médicament pour afficher les officines disponibles.</p>';
        return;
    }

    medHeader.innerHTML = `
        <div style="background:#ecfdf5; border:1px solid #a7f3d0; border-radius:12px; padding:14px 18px; margin-bottom:16px; display:flex; justify-content:space-between; align-items:center; flex-wrap:wrap; gap:10px;">
            <div>
                <div style="font-weight:800; color:#065f46; font-size:1.15rem;">💊 ${state.selectedMedicament.nomCommercial} ${state.selectedMedicament.dosage}</div>
                <div style="font-size:0.85rem; color:#047857;">Forme : ${state.selectedMedicament.forme} · DCI : <strong>${state.selectedMedicament.substanceActiveValidee}</strong></div>
            </div>
            <div style="font-size:0.85rem; color:#065f46; font-weight:700;">
                ${dispos.length} officine(s) déclarée(s) à Lomé
            </div>
        </div>
    `;

    if (dispos.length === 0) {
        container.innerHTML = `
            <div class="pharmacy-card" style="text-align: center; padding: 30px;">
                <p style="font-size: 1.1rem; font-weight: 700; color: #b45309; margin-bottom: 8px;">
                    ⚠️ Aucune disponibilité confirmée actuellement pour ce médicament dans ce quartier.
                </p>
                <p style="font-size: 0.9rem; color: #738782; margin-bottom: 16px;">
                    Vous pouvez élargir le filtre de quartier ou contacter directement les pharmacies de garde de Lomé ci-dessous.
                </p>
                <button class="btn btn-coral" onclick="switchTab('gardes')">Consulter les pharmacies de garde</button>
            </div>
        `;
        return;
    }

    const sorted = [...dispos].sort((a, b) => {
        const order = { 'CONFIRMEE_RECEMMENT': 1, 'A_CONFIRMER': 2, 'INFORMATION_INDISPONIBLE': 3, 'NON_DISPONIBLE': 4 };
        return (order[a.statut] || 99) - (order[b.statut] || 99);
    });

    let html = '';
    sorted.forEach(d => {
        const dateObj = new Date(d.dateConfirmation);
        const formattedDate = dateObj.toLocaleDateString('fr-FR', {
            day: '2-digit', month: '2-digit', year: 'numeric',
            hour: '2-digit', minute: '2-digit'
        });

        const statusClass = `status-${d.statut}`;
        let statusIcon = '🟢';
        if (d.statut === 'A_CONFIRMER') statusIcon = '🟡';
        if (d.statut === 'NON_DISPONIBLE') statusIcon = '🔴';

        // Lien WhatsApp
        const cleanPhone = (d.pharmacieTelephone || '').replace(/[^0-9]/g, '');
        const waText = encodeURIComponent(`Bonjour, je vous contacte via PharmaHelp au sujet de la disponibilité de : ${d.nomCommercial} (${d.dosage}). Confirmez-vous que vous l'avez en stock ? Merci.`);
        const waLink = `https://wa.me/${cleanPhone}?text=${waText}`;

        html += `
            <div class="pharmacy-card" id="card-pharma-${d.pharmacieId}">
                <div class="pharmacy-header">
                    <div>
                        <div class="pharmacy-name">${d.pharmacieNom}</div>
                        <div style="font-size: 0.85rem; color: #738782;">📍 ${d.pharmacieAdresse} (${d.pharmacieQuartier})</div>
                    </div>
                    <div style="display:flex; gap:6px;">
                        ${d.pharmacieDeGarde ? '<span class="badge-garde">⚡ De garde</span>' : ''}
                        <span class="badge-verified">✓ Vérifiée</span>
                    </div>
                </div>

                <div>
                    <span class="status-pill ${statusClass}">
                        ${statusIcon} ${d.statutLibelle}
                    </span>
                </div>

                <div style="font-size:0.8rem; color:#738782; margin-bottom:12px;">
                    🕒 Déclaré le <strong>${formattedDate}</strong> par <em>${d.utilisateurConfirmateur || 'Responsable'}</em>
                    ${d.statut === 'A_CONFIRMER' ? '<br/><span style="color: #b45309;">⚠️ Déclaration datant de plus de 48h. Confirmation préalable requise.</span>' : ''}
                </div>

                <div class="pharmacy-actions">
                    <a href="tel:${d.pharmacieTelephone}" class="btn btn-call" title="Appeler pour confirmation">
                        📞 Appeler
                    </a>
                    <a href="${waLink}" target="_blank" class="btn btn-whatsapp" title="WhatsApp direct">
                        💬 WhatsApp
                    </a>
                    <button class="btn btn-reserve" onclick="openWithdrawalModal('${escapeHtml(d.pharmacieNom)}', '${escapeHtml(d.pharmacieAdresse)}', '${escapeHtml(d.pharmacieTelephone)}', '${escapeHtml(d.nomCommercial)} ${escapeHtml(d.dosage)}')">
                        🛍️ Préparer retrait
                    </button>
                    <a href="https://www.google.com/maps/dir/?api=1&destination=${d.pharmacieLatitude},${d.pharmacieLongitude}" target="_blank" class="btn btn-outline">
                        🗺️ Itinéraire
                    </a>
                    <button style="background:transparent; border:none; color:#94a3b8; font-size:0.78rem; cursor:pointer; margin-left:auto;" onclick="openReportModal('DISPONIBILITE', ${d.id}, '${d.pharmacieNom} - ${d.nomCommercial}')">
                        ⚠️ Signaler
                    </button>
                </div>
            </div>
        `;
    });

    container.innerHTML = html;
}

function escapeHtml(text) {
    if (!text) return '';
    return text.replace(/'/g, "\\'");
}

function renderZeroResults(query) {
    const medHeader = document.getElementById('selectedMedicineHeader');
    const container = document.getElementById('searchResultsContainer');
    medHeader.innerHTML = '';
    container.innerHTML = `
        <div class="pharmacy-card" style="text-align: center; padding: 30px;">
            <p style="font-size: 1.1rem; font-weight: 800; color: #173f38; margin-bottom: 6px;">
                Aucun médicament correspondant à « ${query} » dans le catalogue officiel.
            </p>
            <p style="font-size: 0.88rem; color: #738782; margin-bottom: 16px;">
                Essayez avec la dénomination commune ou choisissez l'un des médicaments essentiels ci-dessous :
            </p>
            <div style="display: flex; gap: 8px; justify-content: center; flex-wrap: wrap;">
                <button class="pill" style="background:#0e7869;" onclick="triggerQuickSearch('Paracétamol')">Paracétamol</button>
                <button class="pill" style="background:#0e7869;" onclick="triggerQuickSearch('Coartem')">Coartem</button>
                <button class="pill" style="background:#0e7869;" onclick="triggerQuickSearch('Amoxicilline')">Amoxicilline</button>
            </div>
        </div>
    `;
}

function focusCard(pharmaId) {
    const el = document.getElementById(`card-pharma-${pharmaId}`);
    if (el) {
        el.scrollIntoView({ behavior: 'smooth', block: 'center' });
        el.style.borderColor = '#0e7869';
        el.style.backgroundColor = '#f2f8f6';
        setTimeout(() => {
            el.style.borderColor = '#d4e1de';
            el.style.backgroundColor = '#ffffff';
        }, 1500);
    }
}

// ============================================================================
// MODAL DE PRÉPARATION DE RETRAIT (CODE UNIQUE FIGMA)
// ============================================================================
function generateWithdrawalCode() {
    const randomDigits = Math.floor(1000 + Math.random() * 9000);
    return `PH-${randomDigits}`;
}

function openWithdrawalModal(pharmaNom, address, phone, medicineName) {
    const code = generateWithdrawalCode();
    state.activeWithdrawal = { code, pharmaNom, phone, medicineName };

    document.getElementById('withdrawalPharmaName').innerText = pharmaNom;
    document.getElementById('withdrawalCodeText').innerText = code;
    document.getElementById('withdrawalMedicineName').innerText = medicineName;
    document.getElementById('withdrawalPharmaAddress').innerText = address;
    document.getElementById('withdrawalPharmaHours').innerText = "Horaires habituels";

    const cleanPhone = phone.replace(/[^0-9]/g, '');
    const msg = encodeURIComponent(`Bonjour ${pharmaNom}, je souhaite préparer mon retrait en pharmacie via PharmaHelp.\nCode de retrait : ${code}\nMédicament : ${medicineName}\nMerci de me confirmer la disponibilité.`);
    document.getElementById('withdrawalWhatsAppBtn').href = `https://wa.me/${cleanPhone}?text=${msg}`;
    document.getElementById('withdrawalCallBtn').href = `tel:${phone}`;

    document.getElementById('withdrawalModal').style.display = 'flex';
}

function closeWithdrawalModal() {
    document.getElementById('withdrawalModal').style.display = 'none';
}

// ============================================================================
// VUE PHARMACIES DE GARDE
// ============================================================================
async function loadGardesView() {
    const container = document.getElementById('gardesListContainer');
    container.innerHTML = '<p class="text-muted">Chargement des officines de garde officielles...</p>';

    try {
        const res = await fetch(`${API_BASE}/public/pharmacies?deGarde=true`);
        const pharmacies = await res.json();

        if (pharmacies.length === 0) {
            container.innerHTML = '<p class="text-muted">Aucune garde renseignée pour cette période.</p>';
            return;
        }

        let html = `
            <div style="background:#fefce8; border:1px solid #fef08a; padding:12px 16px; border-radius:12px; margin-bottom:20px; font-size:0.9rem; color:#854d0e;">
                📢 <strong>Source officielle</strong> : Données conformes au tableau de garde de l'Ordre National des Pharmaciens du Togo et de l'INAM.
                Règle de sécurité : <em>Appelez impérativement avant tout déplacement nocturne.</em>
            </div>
            <div class="results-list">
        `;

        pharmacies.forEach(p => {
            const cleanPhone = (p.telephone || '').replace(/[^0-9]/g, '');
            const waLink = `https://wa.me/${cleanPhone}?text=${encodeURIComponent('Bonjour, je vous contacte via PharmaHelp au sujet de votre service de garde. Êtes-vous ouvert actuellement ?')}`;

            html += `
                <div class="pharmacy-card">
                    <div class="pharmacy-header">
                        <div>
                            <div class="pharmacy-name">${p.nom}</div>
                            <div style="font-size:0.85rem; color:#738782;">📍 ${p.adresse} (${p.quartier})</div>
                        </div>
                        <span class="badge-garde">⚡ De Garde 24h/24</span>
                    </div>
                    <div style="margin: 8px 0; font-size: 0.9rem;">
                        ⏰ Horaires de garde : <strong>${p.horairesTexte}</strong>
                    </div>
                    <div class="pharmacy-actions">
                        <a href="tel:${p.telephone}" class="btn btn-call">📞 Appeler (${p.telephone})</a>
                        <a href="${waLink}" target="_blank" class="btn btn-whatsapp">💬 WhatsApp</a>
                        <a href="https://www.google.com/maps/dir/?api=1&destination=${p.latitude},${p.longitude}" target="_blank" class="btn btn-outline">🗺️ Itinéraire</a>
                    </div>
                </div>
            `;
        });
        html += '</div>';
        container.innerHTML = html;
    } catch (e) {
        container.innerHTML = '<p style="color:red;">Erreur lors du chargement des gardes.</p>';
    }
}

// ============================================================================
// MODAL SIGNALEMENT CITOYEN
// ============================================================================
function openReportModal(typeObjet, objetId, titre) {
    state.selectedPharmacieForReport = { typeObjet, objetId };
    document.getElementById('reportSubject').innerText = titre;
    document.getElementById('reportModal').style.display = 'flex';
}

function closeReportModal() {
    document.getElementById('reportModal').style.display = 'none';
}

async function submitSignalement() {
    const motif = document.getElementById('reportMotif').value;
    const desc = document.getElementById('reportComment').value.trim();

    if (!state.selectedPharmacieForReport) return;

    try {
        const res = await fetch(`${API_BASE}/public/signalements`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                typeObjet: state.selectedPharmacieForReport.typeObjet,
                objetId: state.selectedPharmacieForReport.objetId,
                motif: motif,
                descriptionMinimisee: desc
            })
        });

        if (res.ok) {
            alert('Merci ! Votre signalement a été transmis à l\'équipe PharmaHelp.');
            closeReportModal();
            document.getElementById('reportComment').value = '';
        } else {
            alert('Une erreur est survenue lors de l\'envoi du signalement.');
        }
    } catch (e) {
        console.error(e);
        alert('Erreur réseau.');
    }
}

// ============================================================================
// AUTHENTIFICATION & SESSIONS — avec fallback démo hors-ligne pour Vercel
// ============================================================================

// Comptes démo codés en dur pour Vercel (backend non disponible en statique)
const DEMO_USERS = {
    'admin@pharmafind.tg': {
        password: 'Admin123!',
        role: 'ROLE_ADMIN',
        nomAffichage: 'Administrateur ADN',
        pharmacieId: null,
        pharmacieNom: null
    },
    'pharmacien.golfe@pharmafind.tg': {
        password: 'Pharmacie123!',
        role: 'ROLE_PHARMACIE',
        nomAffichage: 'Dr. Kueviakoe',
        pharmacieId: 1,
        pharmacieNom: 'Pharmacie du Golfe'
    },
    'pharmacien.be@pharmafind.tg': {
        password: 'Pharmacie123!',
        role: 'ROLE_PHARMACIE',
        nomAffichage: 'Dr. Mensah',
        pharmacieId: 2,
        pharmacieNom: 'Pharmacie de Bé'
    }
};

async function checkSession() {
    if (!state.token) {
        updateAuthUI(null);
        return;
    }

    // Mode démo hors-ligne (Vercel statique)
    if (state.token === 'DEMO_TOKEN') {
        const savedUser = localStorage.getItem('pharmahelp_demo_user');
        if (savedUser) {
            state.currentUser = JSON.parse(savedUser);
            updateAuthUI(state.currentUser);
        } else {
            logout();
        }
        return;
    }

    try {
        const res = await fetch(`${API_BASE}/auth/me`, {
            headers: { 'Authorization': `Bearer ${state.token}` }
        });
        if (res.ok) {
            state.currentUser = await res.json();
            updateAuthUI(state.currentUser);
        } else {
            logout();
        }
    } catch (e) {
        // Backend inaccessible → déconnexion silencieuse
        logout();
    }
}

function updateAuthUI(user) {
    const authStatusEl = document.getElementById('authStatusIndicator');
    if (!authStatusEl) return;

    if (user) {
        const badge = state.token === 'DEMO_TOKEN' ? ' <span style="background:#fef9c3;color:#854d0e;border-radius:4px;padding:2px 6px;font-size:0.7rem;font-weight:700;">DÉMO</span>' : '';
        authStatusEl.innerHTML = `
            <div style="display:flex; align-items:center; gap:8px;">
                <span style="font-size:0.85rem; font-weight:700; color:#0e7869;">👤 ${user.nomAffichage} (${user.role.replace('ROLE_', '')})${badge}</span>
                <button onclick="logout()" style="background:transparent; border:1px solid #d4e1de; border-radius:4px; padding:3px 8px; font-size:0.75rem; cursor:pointer;">Déconnexion</button>
            </div>
        `;
    } else {
        authStatusEl.innerHTML = `
            <button onclick="switchTab('pro')" class="btn btn-coral" style="padding:6px 14px; font-size:0.82rem;">Espace Officine / Admin</button>
        `;
    }
}

async function loginUser(identifiant, motDePasse) {
    // 1. Essayer le vrai backend d'abord
    try {
        const res = await fetch(`${API_BASE}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ identifiant, motDePasse })
        });

        if (res.ok) {
            const data = await res.json();
            state.token = data.token;
            localStorage.setItem('pharmahelp_token', data.token);
            await checkSession();
            if (data.role === 'ROLE_ADMIN') {
                switchTab('admin');
            } else {
                switchTab('pro');
            }
            return;
        }
        // Si le backend répond mais avec erreur auth
        if (res.status === 401 || res.status === 403) {
            // Ne pas tomber sur le fallback démo si le backend est dispo
            alert('Identifiants incorrects.');
            return;
        }
    } catch (e) {
        // Backend inaccessible (Vercel statique) → passer au mode démo
        console.warn('Backend indisponible, tentative de connexion en mode démo local…');
    }

    // 2. Fallback : authentification locale démo
    const demoUser = DEMO_USERS[identifiant.trim().toLowerCase()];
    if (demoUser && demoUser.password === motDePasse) {
        state.token = 'DEMO_TOKEN';
        state.currentUser = {
            nomAffichage: demoUser.nomAffichage,
            role: demoUser.role,
            pharmacieId: demoUser.pharmacieId,
            pharmacieNom: demoUser.pharmacieNom
        };
        localStorage.setItem('pharmahelp_token', 'DEMO_TOKEN');
        localStorage.setItem('pharmahelp_demo_user', JSON.stringify(state.currentUser));
        updateAuthUI(state.currentUser);
        if (demoUser.role === 'ROLE_ADMIN') {
            switchTab('admin');
        } else {
            switchTab('pro');
        }
    } else {
        alert('Identifiants incorrects.\n\n📋 Comptes démo disponibles :\n• pharmacien.golfe@pharmafind.tg / Pharmacie123!\n• pharmacien.be@pharmafind.tg / Pharmacie123!\n• admin@pharmafind.tg / Admin123!');
    }
}

function logout() {
    state.token = null;
    state.currentUser = null;
    localStorage.removeItem('pharmahelp_token');
    localStorage.removeItem('pharmahelp_demo_user');
    updateAuthUI(null);
    switchTab('search');
}

// ============================================================================
// ESPACE PROFESSIONNEL (PHARMACIEN)
// ============================================================================
async function loadProView() {
    const container = document.getElementById('proViewContainer');

    if (!state.currentUser || (state.currentUser.role !== 'ROLE_PHARMACIE' && state.currentUser.role !== 'ROLE_ADMIN')) {
        renderProLoginForm(container);
        return;
    }

    // ── Mode démo hors-ligne (Vercel statique) ──────────────────────────────
    if (state.token === 'DEMO_TOKEN') {
        const pharmacieNom = state.currentUser.pharmacieNom || 'Pharmacie du Golfe';
        const demoPharma = {
            nom: pharmacieNom,
            adresse: '15, Rue du Commerce',
            quartier: 'Lomé-Golfe',
            telephone: '+228 90 00 00 01',
            horairesTexte: '07h30 - 22h00',
            enGardeActuellement: true
        };
        // Filtrer les dispos correspondant à cette pharmacie démo
        const demoDispos = FALLBACK_DISPOS
            .filter(d => d.pharmacieId === (state.currentUser.pharmacieId || 1))
            .map(d => {
                const med = FALLBACK_MEDS.find(m => m.id === d.medicamentId) || {};
                return {
                    medicamentId: d.medicamentId,
                    nomCommercial: med.nomCommercial || 'Médicament',
                    substanceActiveValidee: med.substanceActiveValidee || '-',
                    dosage: med.dosage || '-',
                    forme: med.forme || '-',
                    statut: d.statut,
                    statutLibelle: d.statut === 'CONFIRMEE_RECEMMENT' ? '✅ Disponible' : d.statut === 'NON_DISPONIBLE' ? '❌ Rupture' : '🟡 À confirmer',
                    dateConfirmation: new Date().toISOString()
                };
            });
        const demoAudits = [
            { dateAction: new Date().toISOString(), utilisateurNom: state.currentUser.nomAffichage, action: 'CONNEXION_DEMO', resultat: 'Mode démo Vercel activé' }
        ];
        renderProDashboard(container, demoPharma, demoDispos, FALLBACK_MEDS, demoAudits);
        return;
    }
    // ────────────────────────────────────────────────────────────────────────

    container.innerHTML = '<p class="text-muted">Chargement de votre officine...</p>';

    try {
        const [pharmaRes, disposRes, medsRes, auditRes] = await Promise.all([
            fetch(`${API_BASE}/pro/ma-pharmacie`, { headers: { 'Authorization': `Bearer ${state.token}` } }),
            fetch(`${API_BASE}/pro/disponibilites`, { headers: { 'Authorization': `Bearer ${state.token}` } }),
            fetch(`${API_BASE}/public/medicaments`),
            fetch(`${API_BASE}/pro/audit`, { headers: { 'Authorization': `Bearer ${state.token}` } })
        ]);

        if (!pharmaRes.ok) {
            container.innerHTML = '<p class="text-muted">Aucune officine rattachée à ce compte.</p>';
            return;
        }

        const pharma = await pharmaRes.json();
        const dispos = await disposRes.json();
        const allMeds = await medsRes.json();
        const audits = await auditRes.json();

        renderProDashboard(container, pharma, dispos, allMeds, audits);
    } catch (e) {
        console.error(e);
        container.innerHTML = '<p style="color:red;">Erreur lors du chargement de l\'espace professionnel.</p>';
    }
}


function renderProLoginForm(container) {
    container.innerHTML = `
        <div style="max-width:440px; margin:30px auto; background:white; padding:30px; border-radius:16px; border:1px solid #d4e1de; box-shadow:var(--shadow-lg);">
            <div style="display:flex; align-items:center; gap:8px; margin-bottom:16px;">
                <div class="brand-icon" style="width:36px; height:36px; font-size:1.1rem;">✚</div>
                <strong style="font-size:1.25rem; color:#153f38;">Pharma<span style="color:#0e7869;">Help</span> Pro</strong>
            </div>
            <h3 style="font-size:1.3rem; font-weight:800; margin-bottom:4px; color:#173f38;">Connexion Officine</h3>
            <p style="font-size:0.85rem; color:#738782; margin-bottom:20px;">Accès sécurisé pour la mise à jour des disponibilités de stock.</p>

            <form onsubmit="event.preventDefault(); loginUser(document.getElementById('proLoginEmail').value, document.getElementById('proLoginPass').value);">
                <div class="form-group">
                    <label>Adresse e-mail</label>
                    <input type="email" id="proLoginEmail" class="form-control" required placeholder="vous@exemple.tg" />
                </div>
                <div class="form-group">
                    <label>Mot de passe</label>
                    <input type="password" id="proLoginPass" class="form-control" required placeholder="••••••••" />
                </div>
                <button type="submit" class="btn-coral" style="width:100%; margin-top:8px; padding:12px; justify-content:center;">Accéder à PharmaHelp Pro</button>
            </form>

            <div style="margin-top:24px; padding-top:16px; border-top:1px dashed #cbd5e1; font-size:0.85rem;">
                <strong>Comptes démo officines (Lomé) :</strong><br/>
                <button onclick="loginUser('pharmacien.golfe@pharmafind.tg', 'Pharmacie123!')" style="margin-top:6px; background:#f1f5f9; border:1px solid #cbd5e1; padding:6px 10px; border-radius:6px; font-size:0.8rem; cursor:pointer;">
                    🔑 Dr. Kueviakoe (Pharmacie du Golfe)
                </button>
                <button onclick="loginUser('pharmacien.be@pharmafind.tg', 'Pharmacie123!')" style="margin-top:6px; background:#f1f5f9; border:1px solid #cbd5e1; padding:6px 10px; border-radius:6px; font-size:0.8rem; cursor:pointer;">
                    🔑 Dr. Mensah (Pharmacie de Bé)
                </button>
            </div>
        </div>
    `;
}

function renderProDashboard(container, pharma, dispos, allMeds, audits) {
    const dispoMedIds = new Set(dispos.map(d => d.medicamentId));
    const unlistedMeds = allMeds.filter(m => !dispoMedIds.has(m.id));

    let html = `
        <div style="margin-bottom:24px; background:white; padding:22px; border-radius:16px; border:1px solid #d4e1de; display:flex; justify-content:space-between; align-items:center; flex-wrap:wrap; gap:12px; box-shadow:var(--shadow);">
            <div>
                <h2 style="font-size:1.45rem; font-weight:800; color:#153f38;">🏥 ${pharma.nom}</h2>
                <div style="font-size:0.9rem; color:#738782;">📍 ${pharma.adresse} (${pharma.quartier}) · 📞 ${pharma.telephone}</div>
                <div style="font-size:0.85rem; margin-top:4px;">⏰ Horaires déclarés : <strong>${pharma.horairesTexte}</strong></div>
            </div>
            <div>
                <button class="btn btn-outline" onclick="promptUpdateHoraires()">✏️ Modifier les horaires</button>
            </div>
        </div>

        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:16px; flex-wrap:wrap; gap:10px;">
            <h3 style="font-size:1.2rem; font-weight:800; color:#173f38;">Gestion des stocks en 1 clic</h3>
            <div style="display:flex; gap:8px;">
                <select id="addMedicamentSelect" class="form-control" style="max-width:320px;">
                    <option value="">+ Déclarer un médicament du catalogue...</option>
                    ${unlistedMeds.map(m => `<option value="${m.id}">${m.nomCommercial} ${m.dosage} (${m.forme})</option>`).join('')}
                </select>
                <button class="btn btn-coral" onclick="addMedicineToPharmacie()">Ajouter</button>
            </div>
        </div>

        <div class="table-responsive" style="margin-bottom:30px;">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Médicament</th>
                        <th>Dosage & Forme</th>
                        <th>Statut Actuel</th>
                        <th>Dernière Confirmation</th>
                        <th style="text-align:right;">Bascule rapide</th>
                    </tr>
                </thead>
                <tbody>
    `;

    dispos.forEach(d => {
        const dateObj = new Date(d.dateConfirmation);
        const dateStr = dateObj.toLocaleDateString('fr-FR', { day: '2-digit', month: '2-digit', hour: '2-digit', minute: '2-digit' });
        const statusClass = `status-${d.statut}`;

        html += `
            <tr>
                <td><strong>${d.nomCommercial}</strong><br/><span style="font-size:0.75rem; color:#738782;">DCI: ${d.substanceActiveValidee}</span></td>
                <td>${d.dosage} · ${d.forme}</td>
                <td><span class="status-pill ${statusClass}" style="font-size:0.78rem;">${d.statutLibelle}</span></td>
                <td>${dateStr}</td>
                <td style="text-align:right;">
                    <button onclick="updateDispoStatus(${d.medicamentId}, 'CONFIRMEE_RECEMMENT')" style="background:#dcfce7; border:1px solid #86efac; color:#166534; font-weight:700; border-radius:6px; padding:5px 9px; cursor:pointer; font-size:0.78rem;">🟢 DISPONIBLE</button>
                    <button onclick="updateDispoStatus(${d.medicamentId}, 'NON_DISPONIBLE')" style="background:#fee2e2; border:1px solid #fca5a5; color:#991b1b; font-weight:700; border-radius:6px; padding:5px 9px; cursor:pointer; font-size:0.78rem;">🔴 RUPTURE</button>
                    <button onclick="updateDispoStatus(${d.medicamentId}, 'A_CONFIRMER')" style="background:#fef9c3; border:1px solid #fde047; color:#854d0e; font-weight:700; border-radius:6px; padding:5px 9px; cursor:pointer; font-size:0.78rem;">🟡 À CONFIRMER</button>
                </td>
            </tr>
        `;
    });

    html += `
                </tbody>
            </table>
        </div>

        <h3 style="font-size:1.1rem; font-weight:800; margin-bottom:12px; color:#173f38;">📜 Journal d'audit de votre officine (Traçabilité)</h3>
        <div class="table-responsive">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Auteur</th>
                        <th>Action</th>
                        <th>Détails</th>
                    </tr>
                </thead>
                <tbody>
    `;

    audits.slice(0, 10).forEach(a => {
        const dStr = new Date(a.dateAction).toLocaleDateString('fr-FR', { day: '2-digit', month: '2-digit', hour: '2-digit', minute: '2-digit' });
        html += `
            <tr>
                <td style="white-space:nowrap;">${dStr}</td>
                <td>${a.utilisateurNom || 'Système'}</td>
                <td><span style="font-weight:700; font-size:0.8rem;">${a.action}</span></td>
                <td>${a.resultat || ''}</td>
            </tr>
        `;
    });

    html += `
                </tbody>
            </table>
        </div>
    `;

    container.innerHTML = html;
}

async function updateDispoStatus(medicamentId, newStatus) {
    try {
        const res = await fetch(`${API_BASE}/pro/disponibilites`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${state.token}`
            },
            body: JSON.stringify({ medicamentId, statut: newStatus })
        });
        if (res.ok) {
            await loadProView();
            if (state.selectedMedicament) {
                await loadDisponibilitesForMedicament(state.selectedMedicament.id);
            }
        } else {
            alert('Erreur lors de la mise à jour de la disponibilité.');
        }
    } catch (e) {
        console.error(e);
        alert('Erreur réseau.');
    }
}

async function addMedicineToPharmacie() {
    const medId = document.getElementById('addMedicamentSelect').value;
    if (!medId) return;
    await updateDispoStatus(Number(medId), 'CONFIRMEE_RECEMMENT');
}

async function promptUpdateHoraires() {
    const nouv = prompt('Saisissez les nouveaux horaires d\'ouverture habituels : (ex: 07h30 - 22h00)');
    if (!nouv || !nouv.trim()) return;

    try {
        const res = await fetch(`${API_BASE}/pro/pharmacie/horaires`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${state.token}`
            },
            body: JSON.stringify({ horairesTexte: nouv.trim() })
        });
        if (res.ok) {
            alert('Horaires mis à jour.');
            await loadProView();
        }
    } catch (e) {
        alert('Erreur de mise à jour.');
    }
}

// ============================================================================
// ESPACE ADMINISTRATION
// ============================================================================
async function loadAdminView() {
    const container = document.getElementById('adminViewContainer');

    if (!state.currentUser || state.currentUser.role !== 'ROLE_ADMIN') {
        renderAdminLoginForm(container);
        return;
    }

    // ── Mode démo hors-ligne (Vercel statique) ──────────────────────────────
    if (state.token === 'DEMO_TOKEN') {
        // Utiliser les données statiques de fallback pour le tableau admin
        const demoPharmacies = [
            { id: 1, nom: 'Pharmacie du Golfe', adresse: '15 Rue du Commerce', quartier: 'Golfo', telephone: '+228 90 00 00 01' },
            { id: 2, nom: 'Pharmacie de Bé', adresse: '24 Avenue des Champs', quartier: 'Bé', telephone: '+228 90 00 00 02' }
        ];
        const demoSignalements = [];
        const demoAudits = [
            { dateAction: new Date().toISOString(), utilisateurNom: state.currentUser.nomAffichage, action: 'CONNEXION_DEMO', resultat: 'Mode démo Vercel activé' }
        ];
        const demoMeds = FALLBACK_MEDS;
        renderAdminDashboard(container, demoPharmacies, demoSignalements, demoAudits, demoMeds);
        return;
    }
    // ────────────────────────────────────────────────────────────────────────

    container.innerHTML = '<p class="text-muted">Chargement de la modération...</p>';

    try {
        const [pharmaciesRes, signalementsRes, auditsRes, medsRes] = await Promise.all([
            fetch(`${API_BASE}/admin/pharmacies`, { headers: { 'Authorization': `Bearer ${state.token}` } }),
            fetch(`${API_BASE}/admin/signalements`, { headers: { 'Authorization': `Bearer ${state.token}` } }),
            fetch(`${API_BASE}/admin/audit`, { headers: { 'Authorization': `Bearer ${state.token}` } }),
            fetch(`${API_BASE}/public/medicaments`)
        ]);

        const pharmacies = await pharmaciesRes.json();
        const signalements = await signalementsRes.json();
        const audits = await auditsRes.json();
        const meds = await medsRes.json();

        renderAdminDashboard(container, pharmacies, signalements, audits, meds);
    } catch (e) {
        console.error(e);
        container.innerHTML = '<p style="color:red;">Erreur lors du chargement de l\'administration.</p>';
    }
}

function renderAdminLoginForm(container) {
    container.innerHTML = `
        <div style="max-width:440px; margin:30px auto; background:white; padding:30px; border-radius:16px; border:1px solid #d4e1de; box-shadow:var(--shadow-lg);">
            <h3 style="font-size:1.3rem; font-weight:800; margin-bottom:6px; color:#153f38;">Administration PharmaHelp</h3>
            <p style="font-size:0.85rem; color:#738782; margin-bottom:20px;">Connexion réservée aux gestionnaires de la plateforme.</p>

            <form onsubmit="event.preventDefault(); loginUser(document.getElementById('adminLoginEmail').value, document.getElementById('adminLoginPass').value);">
                <div class="form-group">
                    <label>Identifiant Administrateur</label>
                    <input type="email" id="adminLoginEmail" class="form-control" required placeholder="admin@pharmafind.tg" />
                </div>
                <div class="form-group">
                    <label>Mot de passe</label>
                    <input type="password" id="adminLoginPass" class="form-control" required placeholder="••••••••" />
                </div>
                <button type="submit" class="btn btn-primary" style="width:100%; margin-top:10px; padding:12px; background:#153f38; border-radius:8px;">Connexion Administrateur</button>
            </form>

            <div style="margin-top:24px; padding-top:16px; border-top:1px dashed #cbd5e1; font-size:0.85rem;">
                <strong>Identifiants de test démo :</strong><br/>
                <button onclick="loginUser('admin@pharmafind.tg', 'Admin123!')" style="margin-top:6px; background:#f1f5f9; border:1px solid #cbd5e1; padding:6px 10px; border-radius:6px; font-size:0.8rem; cursor:pointer;">
                    🔑 Connexion Administrateur ADN
                </button>
            </div>
        </div>
    `;
}

function renderAdminDashboard(container, pharmacies, signalements, audits, meds) {
    let html = `
        <div style="display:grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap:16px; margin-bottom:24px;">
            <div style="background:white; border:1px solid #d4e1de; border-radius:14px; padding:20px; box-shadow:var(--shadow);">
                <div style="font-size:2rem; font-weight:800; color:#0e7869;">${pharmacies.length}</div>
                <div style="font-size:0.85rem; color:#738782; font-weight:700;">Officines à Lomé</div>
            </div>
            <div style="background:white; border:1px solid #d4e1de; border-radius:14px; padding:20px; box-shadow:var(--shadow);">
                <div style="font-size:2rem; font-weight:800; color:#0e7869;">${meds.length}</div>
                <div style="font-size:0.85rem; color:#738782; font-weight:700;">Médicaments Référencés</div>
            </div>
            <div style="background:white; border:1px solid #d4e1de; border-radius:14px; padding:20px; box-shadow:var(--shadow);">
                <div style="font-size:2rem; font-weight:800; color:#e96652;">${signalements.length}</div>
                <div style="font-size:0.85rem; color:#738782; font-weight:700;">Signalements Citoyens</div>
            </div>
            <div style="background:white; border:1px solid #d4e1de; border-radius:14px; padding:20px; box-shadow:var(--shadow);">
                <div style="font-size:2rem; font-weight:800; color:#153f38;">${audits.length}</div>
                <div style="font-size:0.85rem; color:#738782; font-weight:700;">Événements Journalisés</div>
            </div>
        </div>

        <div style="margin-bottom:24px; background:white; padding:20px; border-radius:16px; border:1px solid #d4e1de; box-shadow:var(--shadow);">
            <h3 style="font-size:1.15rem; font-weight:800; color:#173f38; margin-bottom:12px;">➕ Ajouter un médicament au catalogue national</h3>
            <form onsubmit="event.preventDefault(); submitNewMedicine();" style="display:grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap:12px; align-items:end;">
                <div>
                    <label style="font-size:0.8rem; font-weight:700;">Nom Commercial</label>
                    <input type="text" id="newMedNom" class="form-control" required placeholder="ex: Efferalgan" />
                </div>
                <div>
                    <label style="font-size:0.8rem; font-weight:700;">DCI / Substance</label>
                    <input type="text" id="newMedDci" class="form-control" required placeholder="ex: Paracétamol" />
                </div>
                <div>
                    <label style="font-size:0.8rem; font-weight:700;">Dosage</label>
                    <input type="text" id="newMedDosage" class="form-control" required placeholder="ex: 1000 mg" />
                </div>
                <div>
                    <label style="font-size:0.8rem; font-weight:700;">Forme</label>
                    <input type="text" id="newMedForme" class="form-control" required placeholder="ex: Comprimé effervescent" />
                </div>
                <div>
                    <button type="submit" class="btn btn-coral" style="width:100%; justify-content:center; padding:10px;">Enregistrer</button>
                </div>
            </form>
        </div>

        <h3 style="font-size:1.15rem; font-weight:800; color:#173f38; margin-bottom:12px;">🚨 Signalements reçus des patients</h3>
        <div class="table-responsive" style="margin-bottom:30px;">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Type</th>
                        <th>Motif</th>
                        <th>Description</th>
                        <th>Statut</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
    `;

    if (signalements.length === 0) {
        html += '<tr><td colspan="6" style="text-align:center; color:#738782;">Aucun signalement en attente.</td></tr>';
    } else {
        signalements.forEach(s => {
            const dStr = new Date(s.dateCreation).toLocaleDateString('fr-FR', { day: '2-digit', month: '2-digit', hour: '2-digit', minute: '2-digit' });
            html += `
                <tr>
                    <td>${dStr}</td>
                    <td><span class="badge-verified">${s.typeObjet}</span></td>
                    <td><strong>${s.motif}</strong></td>
                    <td>${s.descriptionMinimisee || '-'}</td>
                    <td><span style="font-weight:700;">${s.statutTraitement}</span></td>
                    <td>
                        <button onclick="updateSignalement(${s.id}, 'TRAITE')" style="background:#dcfce7; border:none; padding:4px 8px; border-radius:4px; font-weight:bold; cursor:pointer;">Valider</button>
                        <button onclick="updateSignalement(${s.id}, 'REJETE')" style="background:#fee2e2; border:none; padding:4px 8px; border-radius:4px; font-weight:bold; cursor:pointer;">Rejeter</button>
                    </td>
                </tr>
            `;
        });
    }

    html += `
                </tbody>
            </table>
        </div>

        <h3 style="font-size:1.15rem; font-weight:800; color:#173f38; margin-bottom:12px;">🛡️ Journal d'Audit Complet (Sécurité & Traçabilité)</h3>
        <div class="table-responsive">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Utilisateur</th>
                        <th>Action</th>
                        <th>Objet</th>
                        <th>Résultat</th>
                    </tr>
                </thead>
                <tbody>
    `;

    audits.slice(0, 15).forEach(a => {
        const dStr = new Date(a.dateAction).toLocaleDateString('fr-FR', { day: '2-digit', month: '2-digit', hour: '2-digit', minute: '2-digit' });
        html += `
            <tr>
                <td style="white-space:nowrap;">${dStr}</td>
                <td>${a.utilisateurNom || 'Système'}</td>
                <td><span style="font-weight:700; font-size:0.8rem; background:#eef4f2; padding:3px 7px; border-radius:4px; color:#153f38;">${a.action}</span></td>
                <td>${a.typeObjet} #${a.objetId || ''}</td>
                <td>${a.resultat || '-'}</td>
            </tr>
        `;
    });

    html += `
                </tbody>
            </table>
        </div>
    `;

    container.innerHTML = html;
}

async function submitNewMedicine() {
    const nom = document.getElementById('newMedNom').value.trim();
    const dci = document.getElementById('newMedDci').value.trim();
    const dosage = document.getElementById('newMedDosage').value.trim();
    const forme = document.getElementById('newMedForme').value.trim();

    try {
        const res = await fetch(`${API_BASE}/admin/medicaments`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${state.token}`
            },
            body: JSON.stringify({
                nomCommercial: nom,
                substanceActiveValidee: dci,
                dosage: dosage,
                forme: forme
            })
        });

        if (res.ok) {
            alert('Médicament ajouté avec succès au catalogue.');
            await loadInitialMedicaments();
            await loadAdminView();
        } else {
            alert('Erreur lors de l\'ajout du médicament.');
        }
    } catch (e) {
        alert('Erreur réseau.');
    }
}

async function updateSignalement(id, statut) {
    try {
        const res = await fetch(`${API_BASE}/admin/signalements/${id}/statut`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${state.token}`
            },
            body: JSON.stringify({ statut })
        });
        if (res.ok) {
            await loadAdminView();
        }
    } catch (e) {
        alert('Erreur de mise à jour.');
    }
}
