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
public class CompteDepotClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public CompteDepotClient(RestTemplate restTemplate, @Value("${depot.service.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    // Méthodes pour CompteDepot
    public List<Map<String, Object>> getAllComptesDepot() {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/CompteDepot")
                .toUriString();
        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map<String, Object>>>() {});
            return response.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("Erreur lors de la récupération des comptes dépôt : " + e.getMessage(), e);
        }
    }

    public Map<String, Object> getCompteDepotById(int id) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/CompteDepot/{id}")
                .buildAndExpand(id)
                .toUriString();
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            return response.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("Erreur lors de la récupération du compte dépôt : " + e.getMessage(), e);
        }
    }

    public Map<String, Object> createCompteDepot(Map<String, Object> compteDepot) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/CompteDepot")
                .toUriString();
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, compteDepot, Map.class);
            return response.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("Erreur lors de la création du compte dépôt : " + e.getMessage(), e);
        }
    }

    // Méthodes pour MouvementDepot (ajoutez d'autres si nécessaire)
    public List<Map<String, Object>> getAllMouvementsDepot() {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/MouvementDepot")
                .toUriString();
        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map<String, Object>>>() {});
            return response.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("Erreur lors de la récupération des mouvements dépôt : " + e.getMessage(), e);
        }
    }

    public Map<String, Object> createMouvementDepot(Map<String, Object> mouvementDepot) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/MouvementDepot")
                .toUriString();
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, mouvementDepot, Map.class);
            return response.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("Erreur lors de la création du mouvement dépôt : " + e.getMessage(), e);
        }
    }

    public Map<String, Object> withdraw(int idCompteDepot, BigDecimal montant, String description) {
        Map<String, Object> mouvement = Map.of(
            "type", "withdraw",
            "montant", montant,
            "description", description,
            "id_compte_depot", idCompteDepot
        );
        return createMouvementDepot(mouvement);
    }
}