package com.centralisateur.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_users")
    private Long idUsers;

    @Column(nullable = false)
    private Integer identifiant;

    @Column(name = "mot_de_passe", nullable = false, length = 100)
    private String motDePasse;

    @Column(nullable = false, length = 50)
    private String role;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    // Constructors
    public Users() {}

    public Users(Integer identifiant, String motDePasse, String role, LocalDateTime dateCreation, Client client) {
        this.identifiant = identifiant;
        this.motDePasse = motDePasse;
        this.role = role;
        this.dateCreation = dateCreation;
        this.client = client;
    }

    // Getters and Setters
    public Long getIdUsers() {
        return idUsers;
    }

    public void setIdUsers(Long idUsers) {
        this.idUsers = idUsers;
    }

    public Integer getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(Integer identifiant) {
        this.identifiant = identifiant;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
