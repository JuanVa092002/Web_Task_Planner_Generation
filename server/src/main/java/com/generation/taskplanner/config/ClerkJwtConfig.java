package com.generation.taskplanner.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
@Profile("!test")
public class ClerkJwtConfig {

    @Bean
    JwtDecoder jwtDecoder(
            @Value("${CLERK_JWK_SET_URI}") String jwkSetUri,
            @Value("${CLERK_ISSUER}") String issuer,
            @Value("${CLERK_AUTHORIZED_PARTIES:}") String authorizedParties
    ) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> withAzp = new AuthorizedPartyValidator(authorizedParties);
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(withIssuer, withAzp));
        return decoder;
    }
}
