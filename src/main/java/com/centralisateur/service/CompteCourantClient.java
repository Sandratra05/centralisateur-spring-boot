package com.centralisateur.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class CompteCourantClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public CompteCourantClient(RestTemplate restTemplate, @Value("${courant.service.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public List<Map<String, Object>> getAllAccounts() {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/compte-courant/all")
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

    public Map<String, Object> createAccount(String idClient, BigDecimal initialSolde) {
        System.out.println("------------- Creating account for client: " + idClient + " with initial balance: " + initialSolde);
        String url = UriComponentsBuilder.fromUriString(baseUrl)
            .path("/compte-courant/create")
            .queryParam("idClient", idClient)
            .queryParam("initialSolde", initialSolde)
            .toUriString();
        ResponseEntity<Map> response = restTemplate.postForEntity(url, null, Map.class);
        return response.getBody();
    }

    public void deposit(Long id, BigDecimal amount, String description) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
            .path("/compte-courant/{id}/deposit")
            .queryParam("amount", amount)
            .queryParam("description", description)
            .buildAndExpand(id)
            .toUriString();
        restTemplate.postForEntity(url, null, Void.class);
    }

    public void withdraw(Long id, BigDecimal amount, String description) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
            .path("/compte-courant/{id}/withdraw")
            .queryParam("amount", amount)
            .queryParam("description", description)
            .buildAndExpand(id)
            .toUriString();
        restTemplate.postForEntity(url, null, Void.class);
    }

    public BigDecimal getBalance(Long id) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
            .path("/compte-courant/{id}/balance")
            .buildAndExpand(id)
            .toUriString();
        ResponseEntity<BigDecimal> response = restTemplate.getForEntity(url, BigDecimal.class);
        return response.getBody();
    }

    public List<Map<String, Object>> getTransactions(Long id) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
            .path("/compte-courant/{id}/transactions")
            .buildAndExpand(id)
            .toUriString();
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        return response.getBody();
    }
}
