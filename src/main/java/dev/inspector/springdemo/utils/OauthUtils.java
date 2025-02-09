package dev.inspector.springdemo.utils;

import dev.inspector.springdemo.dto.AuthUserData;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class OauthUtils {

    public AuthUserData getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var claims =((Jwt) authentication.getPrincipal()).getClaims();
        return AuthUserData.builder()
                .login(authentication.getName())
                .clientId(((ArrayList<String>)claims.get("aud")).get(0))
                .userId(claims.get("userId").toString())
                .deviceId(Optional.ofNullable(claims.get("deviceId"))
                        .map(Object::toString)
                        .orElse(null))
                .appVersion(Optional.ofNullable(claims.get("appVersion"))
                        .map(Object::toString)
                        .orElse(null))
                .build();
    }
}