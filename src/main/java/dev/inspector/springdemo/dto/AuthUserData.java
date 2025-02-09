package dev.inspector.springdemo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthUserData {
    private String userId;
    private String login;
    private String clientId;
    private String deviceId;
    private String appVersion;
}
