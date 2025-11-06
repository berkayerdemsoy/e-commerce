package com.example.payment_service_app.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String , String> kafkaTemplate;

    public void sendMessage(String topic , String message){
        kafkaTemplate.send(topic,message);
    }
}



//@Service
//@RequiredArgsConstructor
//public class KafkaProducerService {
//    private final KafkaTemplate<String , String> kafkaTemplate;
//    private final KeycloakTokenService keycloakTokenService;
//
//    public void sendMessage(String topic , String message) throws JsonProcessingException {
//        String token = keycloakTokenService.getClientCredentialsToken();
//
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("message", message);
//        payload.put("token", token);
//
//        String jsonPayload = new ObjectMapper().writeValueAsString(payload);
//        kafkaTemplate.send(topic, jsonPayload);
//    }
//}
