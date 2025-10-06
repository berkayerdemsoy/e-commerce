package com.example.api_gateway.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements Ordered , GlobalFilter {

    private final JwtService jwtService;

    private final List<String> publicEndpoints = List.of(
            "/auth/register",
            "/auth/login"
    );

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

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (isPublicEndpoint(request)){
            return chain.filter(exchange);
        }

        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            return onError(exchange,HttpStatus.UNAUTHORIZED);
        }

        String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
        if (!authHeader.startsWith("Bearer ")){
            return onError(exchange,HttpStatus.UNAUTHORIZED);
        }
        String token = authHeader.substring(7);

        try{
            jwtService.isTokenValid(token);
        }catch (Exception e){
            return onError(exchange,HttpStatus.UNAUTHORIZED);
        }
        return chain.filter(exchange);

    }

}
