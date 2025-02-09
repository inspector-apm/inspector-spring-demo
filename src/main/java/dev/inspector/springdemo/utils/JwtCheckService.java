package dev.inspector.springdemo.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtCheckService {

    public boolean isVerified() {
        var authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(authentication)
                .map(JwtAuthenticationToken::getToken)
                .map(Jwt::getClaims)
                .filter(claims -> claims.containsKey("verified"))
                .map(claims -> Boolean.parseBoolean(claims.get("verified").toString()))
                .orElse(false);
    }
}