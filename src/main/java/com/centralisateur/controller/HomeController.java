package com.centralisateur.controller;

import com.centralisateur.service.CompteCourantClient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @PersistenceContext
    private EntityManager entityManager;

    private final CompteCourantClient compteCourantClient;

    public HomeController(CompteCourantClient compteCourantClient) {
        this.compteCourantClient = compteCourantClient;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/db-test")
    public String dbTest(Model model) {
        try {
            Query query = entityManager.createNativeQuery("SELECT COUNT(*) FROM client");
            Number result = (Number) query.getSingleResult();
            Long count = result.longValue();
            model.addAttribute("message", "Connexion réussie à la base de données. Nombre de clients : " + count);
        } catch (Exception e) {
            model.addAttribute("message", "Erreur de connexion à la base de données : " + e.getMessage());
        }
        return "db-test";
    }

    // Compte Courant Views
    @GetMapping("/courant")
    public String courantHome() {
        return "courant/index";
    }

    @GetMapping("/pret")
    public String pretHome() {
        return "pret/index";
    }

    @GetMapping("/depot")
    public String depotHome() {
        return "depot/index";
    }
}
