package com.example.api_gateway.authentication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements Ordered, GlobalFilter {

    private final JwtService jwtService;
    private final ReactiveJwtDecoder keycloakJwtDecoder;

    private final List<String> publicEndpoints = List.of(
            "/auth/register",
            "/auth/login",
            "/auth/refresh-token"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 1. Public endpoint kontrolü
        if (publicEndpoints.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        // 2. Authorization header kontrolü
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header for path: {}", path);
            return onError(exchange, HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        // 3. Önce Custom JWT dene (user-service token'ı)
        if (jwtService.isTokenValid(token)) {
            log.debug("Valid custom JWT detected for path: {}", path);
            return chain.filter(exchange);
        }

        // 4. Keycloak OAuth2 token dene (service-to-service)
        return keycloakJwtDecoder.decode(token)
                .flatMap(jwt -> {
                    log.debug("Valid Keycloak JWT detected for path: {}", path);
                    return chain.filter(exchange);
                })
                .onErrorResume(e -> {
                    log.error("JWT validation failed for path: {} - {}", path, e.getMessage());
                    return onError(exchange, HttpStatus.UNAUTHORIZED);
                });
    }

    private boolean isPublicEndpoint(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        return publicEndpoints.stream().anyMatch(path::startsWith);
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
