package com.centralisateur.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    public Map<String, Object> createAccount(String idClient, BigDecimal initialSolde) {
        System.out.println("------------- Creating account for client: " + idClient + " with initial balance: " + initialSolde);
        String url = baseUrl + "/compte-courant/create?idClient=" + idClient + "&initialSolde=" + initialSolde;
        ResponseEntity<Map> response = restTemplate.postForEntity(url, null, Map.class);
        return response.getBody();
    }

    public void deposit(Long id, BigDecimal amount, String description) {
        String url = baseUrl + "/compte-courant/" + id + "/deposit?amount=" + amount + "&description=" + description;
        restTemplate.postForEntity(url, null, Void.class);
    }

    public void withdraw(Long id, BigDecimal amount, String description) {
        String url = baseUrl + "/compte-courant/" + id + "/withdraw?amount=" + amount + "&description=" + description;
        restTemplate.postForEntity(url, null, Void.class);
    }

    public BigDecimal getBalance(Long id) {
        String url = baseUrl + "/compte-courant/" + id + "/balance";
        ResponseEntity<BigDecimal> response = restTemplate.getForEntity(url, BigDecimal.class);
        return response.getBody();
    }

    public List<Map<String, Object>> getTransactions(Long id) {
        String url = baseUrl + "/compte-courant/" + id + "/transactions";
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        return response.getBody();
    }
}
