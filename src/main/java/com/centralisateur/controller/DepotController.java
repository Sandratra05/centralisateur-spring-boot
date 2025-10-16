package com.centralisateur.controller;

import com.centralisateur.entity.Client;
import com.centralisateur.service.ClientService;
import com.centralisateur.service.CompteDepotClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/depot")
public class DepotController {

    private final CompteDepotClient client;

    @Autowired
    private ClientService clientService;

    public DepotController(CompteDepotClient client) {
        this.client = client;
    }

    // Affiche le formulaire de création
    @GetMapping("/create")
    public String showCreateForm() {
        return "depot/create";
    }

    // Traite la soumission du formulaire de création
    @PostMapping("/create")
    public String createAccount(
            @RequestParam int idClient,
            @RequestParam BigDecimal solde,
            @RequestParam BigDecimal tauxInteret,
            Model model
    ) {
        try {
            Optional<Client> clientOpt = clientService.findById((long) idClient);
            if(clientOpt.isEmpty()) {
                model.addAttribute("error", "Le client n'existe pas.");
                return "depot/create";
            }

            // Validation simple
            if (solde.compareTo(BigDecimal.ZERO) < 0) {
                model.addAttribute("error", "Le solde initial doit être ≥ 0.");
                return "depot/create";
            }
            if (tauxInteret.compareTo(BigDecimal.ZERO) < 0) {
                model.addAttribute("error", "Le taux d'intérêt doit être ≥ 0.");
                return "depot/create";
            }

            Map<String, Object> compte = Map.of(
                "idClient", idClient,
                "solde", solde,
                "tauxInteret", tauxInteret
            );
            var account = client.createCompteDepot(compte);
            model.addAttribute("message", "Compte dépôt créé: " + account);
        } catch (Exception ex) {
            model.addAttribute("error", "Échec de création: " + ex.getMessage());
        }

        return "depot/create";
    }

    @GetMapping("/withdraw")
    public String withdrawForm(Model model) {
        List<Map<String, Object>> accounts = client.getAllComptesDepot();
        model.addAttribute("accounts", accounts);
        return "depot/withdraw";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam int id, @RequestParam BigDecimal amount, @RequestParam String description, Model model) {
        try {
            Optional<Client> clientOpt = clientService.findById((long) id);
            if(clientOpt.isEmpty()) {
                model.addAttribute("error", "Le client n'existe pas.");
                return "depot/withdraw";
            }

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                model.addAttribute("error", "Le montant à retirer doit être > 0.");
                return "depot/withdraw";
            }

            client.withdraw(id, amount, description);
            model.addAttribute("message", "Retrait effectué avec succès.");
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du retrait : " + e.getMessage());
        }

        List<Map<String, Object>> accounts = client.getAllComptesDepot();
        model.addAttribute("accounts", accounts);
        return "depot/withdraw";
    }
}
