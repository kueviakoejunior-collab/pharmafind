INSERT INTO pharmacies (nom, adresse, quartier, telephone, latitude, longitude, horaires_texte, statut_validation, date_creation, date_mise_a_jour) VALUES
('Pharmacie Centrale', 'Rue de la Liberté 1', 'Centre', '+228 22 33 44 55', 6.12345, 1.23456, '08:00-20:00', 'VALIDEE', NOW(), NOW()),
('Pharmacie des Fleurs', 'Avenue des Fleurs 12', 'Bé', '+228 22 33 44 56', 6.12456, 1.23567, '08:00-20:00', 'VALIDEE', NOW(), NOW()),
('Pharmacie du Soleil', 'Boulevard du Soleil 5', 'Lomé', '+228 22 33 44 57', 6.12567, 1.23678, '07:30-21:30', 'VALIDEE', NOW(), NOW()),
('Pharmacie du Nord', 'Rue du Nord 22', 'Bè', '+228 22 33 44 58', 6.12678, 1.23789, '07:00-19:00', 'VALIDEE', NOW(), NOW()),
('Pharmacie du Sud', 'Avenue du Sud 9', 'Lomé-Est', '+228 22 33 44 59', 6.12789, 1.23890, '09:00-22:00', 'VALIDEE', NOW(), NOW()),
('Pharmacie de la Mer', 'Boulevard de la Mer 3', 'Coco', '+228 22 33 44 60', 6.12890, 1.23901, '08:30-18:30', 'VALIDEE', NOW(), NOW()),
('Pharmacie du Marché', 'Place du Marché 8', 'Tarako', '+228 22 33 44 61', 6.12901, 1.24012, '07:15-19:15', 'VALIDEE', NOW(), NOW()),
('Pharmacie Esprit', 'Rue de l\'Esprit 4', 'Campus', '+228 22 33 44 62', 6.13012, 1.24123, '08:00-20:00', 'VALIDEE', NOW(), NOW()),
('Pharmacie Horizon', 'Avenue Horizon 7', 'Horizon', '+228 22 33 44 63', 6.13123, 1.24234, '07:45-21:45', 'VALIDEE', NOW(), NOW()),
('Pharmacie Nouvelle', 'Rue Nouvelle 10', 'Nouveau', '+228 22 33 44 64', 6.13234, 1.24345, '08:00-22:00', 'VALIDEE', NOW(), NOW());

INSERT INTO medicaments (nom_commercial, substance_active_validee, forme, dosage, code_reference, statut_validation, date_creation) VALUES
('Paracetamol', 'Paracetamol', 'Comprimé', '500 mg', 'PARA500', 'VALIDE', NOW()),
('Amoxicilline', 'Amoxicilline', 'Gélule', '250 mg', 'AMOX250', 'VALIDE', NOW()),
('Ibuprofen', 'Ibuprofen', 'Comprimé', '200 mg', 'IBU200', 'VALIDE', NOW()),
('Cough Syrup', 'Dextromethorphan', 'Sirop', '10 mg/5 ml', 'COU10', 'VALIDE', NOW()),
('Aspirine', 'Acide Acétylsalicylique', 'Comprimé', '100 mg', 'ASP100', 'VALIDE', NOW()),
('Metformin', 'Metformin', 'Comprimé', '500 mg', 'MET500', 'VALIDE', NOW()),
('Omeprazole', 'Omeprazole', 'Capsule', '20 mg', 'OME20', 'VALIDE', NOW()),
('Loratadine', 'Loratadine', 'Comprimé', '10 mg', 'LOR10', 'VALIDE', NOW()),
('Vitamin C', 'Acide Ascorbique', 'Comprimé', '1000 mg', 'VITC1000', 'VALIDE', NOW()),
('Hydroxychloroquine', 'Hydroxychloroquine', 'Comprimé', '200 mg', 'HYD200', 'VALIDE', NOW());
