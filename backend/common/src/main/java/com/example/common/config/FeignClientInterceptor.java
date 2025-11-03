package com.example.common.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeignClientInterceptor implements RequestInterceptor {

    private final OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;
    private static final String CLIENT_REGISTRATION_ID = "internal-client";

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            String authHeader = attributes.getRequest().getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                template.header("Authorization", authHeader);
                return;
            }
        }

        try {
            OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest
                    .withClientRegistrationId(CLIENT_REGISTRATION_ID)
                    .principal("internal-service")
                    .build();

            OAuth2AuthorizedClient authorizedClient = oAuth2AuthorizedClientManager.authorize(request);
            if (authorizedClient != null && authorizedClient.getAccessToken() != null) {
                String tokenValue = authorizedClient.getAccessToken().getTokenValue();
                template.header("Authorization", "Bearer " + tokenValue);
            } else {
                log.warn("Feign Interceptor : Could not obtain system token.");
            }

        } catch (Exception e) {
            log.error("Feign Interceptor : Error Obtaining system token" + e);
        }


    }
}