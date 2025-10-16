package com.centralisateur.controller;

import com.centralisateur.entity.Client;
import com.centralisateur.service.ClientService;
import com.centralisateur.service.ComptePretClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/pret")
public class PretController {
    private final ComptePretClient client;

    @Autowired
    private ClientService clientService;

    public PretController(ComptePretClient client) {
        this.client = client;
    }

    @GetMapping("/create")
    public String showCreateForm() {
        return "pret/create";
    }

    @PostMapping("/create")
    public String createLoan(@RequestParam Long idClient,
                             @RequestParam BigDecimal montant,
                             @RequestParam BigDecimal tauxPret,
                             @RequestParam String dateDebut,
                             @RequestParam String dateFin,
                             Model model) {
        try {
            Optional<Client> clientOpt = clientService.findById(idClient);
            if(clientOpt.isEmpty()) {
                model.addAttribute("error", "Le client n'existe pas.");
                return "pret/create";
            }


            var pret = client.createLoan(idClient, montant, tauxPret, dateDebut, dateFin);
            model.addAttribute("message", "Prêt créé: " + pret);
        } catch (Exception e) {
            model.addAttribute("error", "Erreur création prêt: " + e.getMessage());
        }
        return "pret/create";
    }

    @GetMapping("/repayment")
    public String showRepaymentForm(Model model) {
        List<Map<String, Object>> accounts = client.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "pret/repayment";
    }

    @PostMapping("/repayment")
    public String makeRepayment(@RequestParam Long id,
                                @RequestParam BigDecimal amount,
                                @RequestParam String modePaiement,
                                Model model) {
        try {
            Optional<Client> clientOpt = clientService.findById(id);
            if(clientOpt.isEmpty()) {
                model.addAttribute("error", "Le client n'existe pas.");
                return "pret/repayment";
            }

            client.makeRepayment(id, amount, modePaiement);
            model.addAttribute("message", "Remboursement effectué.");
        } catch (Exception e) {
            model.addAttribute("error", "Erreur remboursement: " + e.getMessage());
        }
        List<Map<String, Object>> accounts = client.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "pret/repayment";
    }

    @GetMapping("/balance")
    public String showBalanceForm(Model model) {
        List<Map<String, Object>> accounts = client.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "pret/balance";
    }

    @PostMapping("/balance")
    public String getRemainingBalance(@RequestParam Long id, Model model) {
        try {

            Optional<Client> clientOpt = clientService.findById(id);
            if(clientOpt.isEmpty()) {
                model.addAttribute("error", "Le client n'existe pas.");
                return "pret/balance";
            }

            BigDecimal balance = client.getRemainingBalance(id);
            model.addAttribute("balance", balance);
        } catch (Exception e) {
            model.addAttribute("error", "Erreur récupération solde: " + e.getMessage());
        }
        return "pret/balance";
    }

    @GetMapping("/repayments")
    public String showRepaymentsForm(Model model) {
        List<Map<String, Object>> accounts = client.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "pret/repayments";
    }

    @PostMapping("/repayments")
    public String getRepayments(@RequestParam Long id, Model model) {
        try {

            Optional<Client> clientOpt = clientService.findById(id);
            if(clientOpt.isEmpty()) {
                model.addAttribute("error", "Le client n'existe pas.");
                return "pret/repayments";
            }

            List<Map<String, Object>> repayments = client.getRepayments(id);
            model.addAttribute("repayments", repayments);
        } catch (Exception e) {
            model.addAttribute("error", "Erreur récupération remboursements: " + e.getMessage());
        }
        List<Map<String, Object>> accounts = client.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "pret/repayments";
    }
}
