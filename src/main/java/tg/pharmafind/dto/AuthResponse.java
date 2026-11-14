package tg.pharmafind.dto;

import tg.pharmafind.entity.Role;

public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String nomAffichage;
    private String identifiant;
    private Role role;
    private Long pharmacieId;
    private String pharmacieNom;

    public AuthResponse() {
    }

    public AuthResponse(String token, Long id, String nomAffichage, String identifiant, Role role, Long pharmacieId, String pharmacieNom) {
        this.token = token;
        this.id = id;
        this.nomAffichage = nomAffichage;
        this.identifiant = identifiant;
        this.role = role;
        this.pharmacieId = pharmacieId;
        this.pharmacieNom = pharmacieNom;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomAffichage() {
        return nomAffichage;
    }

    public void setNomAffichage(String nomAffichage) {
        this.nomAffichage = nomAffichage;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getPharmacieId() {
        return pharmacieId;
    }

    public void setPharmacieId(Long pharmacieId) {
        this.pharmacieId = pharmacieId;
    }

    public String getPharmacieNom() {
        return pharmacieNom;
    }

    public void setPharmacieNom(String pharmacieNom) {
        this.pharmacieNom = pharmacieNom;
    }
}
