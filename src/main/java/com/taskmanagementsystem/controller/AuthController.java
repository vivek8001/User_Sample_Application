package com.taskmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Authentication Controller for Keycloak Integration
 *
 * NOTE: With Keycloak, authentication is handled by Keycloak server, not this application.
 * This controller provides information endpoints to help clients connect to Keycloak.
 *
 * Users should:
 * 1. Login via Keycloak to get JWT token
 * 2. Include the JWT token in Authorization header: "Bearer <token>"
 * 3. Access protected endpoints in this application
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    /**
     * Returns Keycloak authentication endpoints and configuration info
     * Use this endpoint to discover Keycloak URLs for your frontend application
     *
     * Example response:
     * {
     *   "issuer": "http://localhost:8180/realms/taskmanagementsystem",
     *   "token_endpoint": "http://localhost:8180/realms/taskmanagementsystem/protocol/openid-connect/token",
     *   "authorization_endpoint": "http://localhost:8180/realms/taskmanagementsystem/protocol/openid-connect/auth",
     *   "userinfo_endpoint": "http://localhost:8180/realms/taskmanagementsystem/protocol/openid-connect/userinfo",
     *   "logout_endpoint": "http://localhost:8180/realms/taskmanagementsystem/protocol/openid-connect/logout"
     * }
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getAuthInfo() {
        Map<String, String> authInfo = new HashMap<>();
        authInfo.put("issuer", issuerUri);
        authInfo.put("token_endpoint", issuerUri + "/protocol/openid-connect/token");
        authInfo.put("authorization_endpoint", issuerUri + "/protocol/openid-connect/auth");
        authInfo.put("userinfo_endpoint", issuerUri + "/protocol/openid-connect/userinfo");
        authInfo.put("logout_endpoint", issuerUri + "/protocol/openid-connect/logout");
        authInfo.put("jwks_uri", issuerUri + "/protocol/openid-connect/certs");

        return ResponseEntity.ok(authInfo);
    }

    /**
     * Example endpoint showing how to get a token from Keycloak
     * This is for documentation purposes - actual login happens on Keycloak
     */
    @GetMapping("/how-to-login")
    public ResponseEntity<Map<String, String>> howToLogin() {
        Map<String, String> instructions = new HashMap<>();
        instructions.put("method", "POST");
        instructions.put("url", issuerUri + "/protocol/openid-connect/token");
        instructions.put("content_type", "application/x-www-form-urlencoded");
        instructions.put("body_example", "client_id=spring-boot-app&username=YOUR_USERNAME&password=YOUR_PASSWORD&grant_type=password");
        instructions.put("curl_example",
                "curl -X POST '" + issuerUri + "/protocol/openid-connect/token' " +
                "-H 'Content-Type: application/x-www-form-urlencoded' " +
                "-d 'client_id=spring-boot-app' " +
                "-d 'username=YOUR_USERNAME' " +
                "-d 'password=YOUR_PASSWORD' " +
                "-d 'grant_type=password'");
        instructions.put("note", "Replace YOUR_USERNAME and YOUR_PASSWORD with actual credentials from Keycloak");

        return ResponseEntity.ok(instructions);
    }
}
