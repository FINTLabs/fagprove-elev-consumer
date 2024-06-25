package no.fintlabs;

import org.springframework.security.oauth2.jwt.Jwt;

public class ClaimHelper {

    public static String getEmail(Jwt jwt) {
        return (String) jwt.getClaims().getOrDefault("email", "EmptyEmail");
    }

}
