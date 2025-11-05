package com.example.payment_service_app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class KeycloakTokenService {
    private final RestTemplate rest = new RestTemplate();

    @Value("${keycloak.token-uri}")
    private String tokenUri;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    public String getClientCredentialsToken() {
        MultiValueMap<String,String> form = new LinkedMultiValueMap<>();
        form.add("grant_type","client_credentials");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(form, headers);
        ResponseEntity<Map> resp = rest.postForEntity(tokenUri, request, Map.class);
        if (resp.getStatusCode().is2xxSuccessful() && resp.getBody()!=null) {
            return (String) resp.getBody().get("access_token");
        }
        throw new IllegalStateException("Failed to retrieve token from Keycloak: " + resp);
    }
}

