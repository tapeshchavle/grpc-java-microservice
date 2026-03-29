package com.example.javaservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

/**
 * Controller: UserServerController
 * Purpose: A standard Spring Boot REST Endpoint exposing the Users dataset.
 * Functions as a primary Backend node for any client (like our Python Service or a React App) 
 * connecting over traditional HTTP/1.1 protocols.
 */
@RestController // Injects this class into Spring MVC as a JSON-producing controller
@RequestMapping("/api/users") // Base Path
public class UserServerController {

    /**
     * Endpoint: GET /api/users
     * How it works:
     * 1. Returns an exact hardcoded List of Maps.
     * 2. The enclosing `RestController` annotation ensures that the underlying web engine
     *    intercepts this List and formats it into `application/json` automatically.
     */
    @GetMapping
    public List<Map<String, Object>> getUsers() {
        return List.of(
            Map.of("id", 1, "username", "java_dev", "email", "dev@java.com"),
            Map.of("id", 2, "username", "spring_master", "email", "master@spring.io")
        );
    }
}
