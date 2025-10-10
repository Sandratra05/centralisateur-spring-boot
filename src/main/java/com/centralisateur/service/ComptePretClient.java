package com.centralisateur.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class ComptePretClient {
    private final RestTemplate restTemplate;
    private final String baseUrl;

    public ComptePretClient(RestTemplate restTemplate, @Value("${pret.service.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public Map<String, Object> createLoan(Long idClient, BigDecimal montant, BigDecimal tauxPret, String dateDebut, String dateFin) {
        System.out.println("Creating loan with parameters: idClient=" + idClient + ", montant=" + montant + ", tauxPret=" + tauxPret + ", dateDebut=" + dateDebut + ", dateFin=" + dateFin);
        String url = baseUrl + "/compte-pret/create?idClient=" + idClient + "&montant=" + montant + "&tauxPret=" + tauxPret + "&dateDebut=" + dateDebut + "&dateFin=" + dateFin;
        ResponseEntity<Map> response = restTemplate.postForEntity(url, null, Map.class);
        return response.getBody();
    }

    public void makeRepayment(Long id, BigDecimal amount, String modePaiement) {
        String url = baseUrl + "/compte-pret/" + id + "/repayment?amount=" + amount + "&modePaiement=" + modePaiement;
        restTemplate.postForEntity(url, null, Void.class);
    }

    public BigDecimal getRemainingBalance(Long id) {
        String url = baseUrl + "/compte-pret/" + id + "/balance";
        ResponseEntity<BigDecimal> response = restTemplate.getForEntity(url, BigDecimal.class);
        return response.getBody();
    }

    public List<Map<String, Object>> getRepayments(Long id) {
        String url = baseUrl + "/compte-pret/" + id + "/repayments";
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        return response.getBody();
    }
}
