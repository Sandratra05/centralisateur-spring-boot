package com.centralisateur.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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

    public List<Map<String, Object>> getAllAccounts() {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/compte-pret/all")
                .toUriString();
        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map<String, Object>>>() {});
            return response.getBody();
        } catch (RestClientException e) {
            // Log or handle the error appropriately
            throw new RuntimeException("Erreur lors de la récupération des comptes : " + e.getMessage(), e);
        }
    }

    public Map<String, Object> createLoan(Long idClient, BigDecimal montant, BigDecimal tauxPret, String dateDebut, String dateFin) {
        System.out.println("Creating loan with parameters: idClient=" + idClient + ", montant=" + montant + ", tauxPret=" + tauxPret + ", dateDebut=" + dateDebut + ", dateFin=" + dateFin);
        String url = UriComponentsBuilder.fromUriString(baseUrl)
            .path("/compte-pret/create")
            .queryParam("idClient", idClient)
            .queryParam("montant", montant)
            .queryParam("tauxPret", tauxPret)
            .queryParam("dateDebut", dateDebut)
            .queryParam("dateFin", dateFin)
            .toUriString();
        ResponseEntity<Map> response = restTemplate.postForEntity(url, null, Map.class);
        return response.getBody();
    }

    public void makeRepayment(Long id, BigDecimal amount, String modePaiement) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
            .path("/compte-pret/{id}/repayment")
            .queryParam("amount", amount)
            .queryParam("modePaiement", modePaiement)
            .buildAndExpand(id)
            .toUriString();
        restTemplate.postForEntity(url, null, Void.class);
    }

    public BigDecimal getRemainingBalance(Long id) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
            .path("/compte-pret/{id}/balance")
            .buildAndExpand(id)
            .toUriString();
        ResponseEntity<BigDecimal> response = restTemplate.getForEntity(url, BigDecimal.class);
        return response.getBody();
    }

    public List<Map<String, Object>> getRepayments(Long id) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
            .path("/compte-pret/{id}/repayments")
            .buildAndExpand(id)
            .toUriString();
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        return response.getBody();
    }
}
