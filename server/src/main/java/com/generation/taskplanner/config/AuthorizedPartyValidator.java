package com.generation.taskplanner.config;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthorizedPartyValidator implements OAuth2TokenValidator<Jwt> {

    private final Set<String> allowedParties;

    public AuthorizedPartyValidator(String csv) {
        this.allowedParties = Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .collect(Collectors.toSet());
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        if (allowedParties.isEmpty()) {
            return OAuth2TokenValidatorResult.success();
        }
        String azp = jwt.getClaimAsString("azp");
        if (azp != null && allowedParties.contains(azp)) {
            return OAuth2TokenValidatorResult.success();
        }
        return OAuth2TokenValidatorResult.failure(
                new OAuth2Error("invalid_token", "Invalid authorized party", null)
        );
    }
}
