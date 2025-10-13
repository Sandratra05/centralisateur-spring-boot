package com.centralisateur.controller;

import com.centralisateur.service.CompteCourantClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/courant")
public class CourantController {

    private final CompteCourantClient client;

    public CourantController(CompteCourantClient client) {
        this.client = client;
    }

    // Affiche le formulaire
    @GetMapping("/create")
    public String showCreateForm() {
        return "courant/create";
    }

    // Traite la soumission du formulaire
    @PostMapping("/create")
    public String createAccount(
            @RequestParam String idClient,
            @RequestParam BigDecimal initialSolde,
            Model model
    ) {
        try {
            // Validation simple
            if (initialSolde.compareTo(BigDecimal.ZERO) < 0) {
                model.addAttribute("error", "Le solde initial doit être \u2265 0.");
                return "courant/create";
            }

            var account = client.createAccount(idClient, initialSolde);
            model.addAttribute("message", "Compte créé: " + account);
        } catch (Exception ex) {
            model.addAttribute("error", "Échec de création: " + ex.getMessage());
        }

        return "courant/create";
    }

    @GetMapping("/deposit")
    public String depositForm(Model model) {
        List<Map<String, Object>> accounts = client.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "courant/deposit";
    }
    @PostMapping("/deposit")
    public String deposit(@RequestParam Long id, @RequestParam BigDecimal amount, @RequestParam String description, Model model) {
        try {
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                model.addAttribute("error", "L'argent à déposer doit être \u2265 0.");
                return "courant/deposit";
            }

            client.deposit(id, amount, description);
            model.addAttribute("message", "Dépôt effectué avec succès.");
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du dépôt : " + e.getMessage());
        }

        List<Map<String, Object>> accounts = client.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "courant/deposit";
    }

    @GetMapping("/withdraw")
    public String withdrawForm(Model model) {
        List<Map<String, Object>> accounts = client.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "courant/withdraw";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam Long id, @RequestParam BigDecimal amount, @RequestParam String description, Model model) {
        try {
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                model.addAttribute("error", "L'argent à retirer doit être \u2265 0.");
                return "courant/withdraw";
            }

            client.withdraw(id, amount, description);
            model.addAttribute("message", "Retrait effectué avec succès.");
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du retrait : " + e.getMessage());
        }

        List<Map<String, Object>> accounts = client.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "courant/withdraw";
    }

    @GetMapping("/balance")
    public String balanceForm(Model model) {
        List<Map<String, Object>> accounts = client.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "courant/balance";
    }
    @PostMapping("/balance")
    public String getBalance(@RequestParam Long id, Model model) {
        try {
            BigDecimal balance = client.getBalance(id);
            model.addAttribute("balance", balance);
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la récupération du solde : " + e.getMessage());
        }

        List<Map<String, Object>> accounts = client.getAllAccounts();
        model.addAttribute("accounts", accounts);

        return "courant/balance";
    }

    @GetMapping("/transactions")
    public String transactionsForm(Model model) {
        List<Map<String, Object>> accounts = client.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "courant/transactions";
    }

    @PostMapping("/transactions")
    public String getTransactions(@RequestParam Long id, Model model) {
        try {
            List<Map<String, Object>> transactions = client.getTransactions(id);
            model.addAttribute("transactions", transactions);
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la récupération des transactions : " + e.getMessage());
        }

        List<Map<String, Object>> accounts = client.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "courant/transactions";
    }
}