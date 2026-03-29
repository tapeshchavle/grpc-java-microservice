package com.example.javaservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

/**
 * Controller: ItemServerController
 * Purpose: This acts as a standard Spring Boot REST API server endpoint.
 * When the Python microservice makes a standard HTTP request to Java, this file intercepts and processes it.
 */
@RestController // Tells Spring Boot this class serves JSON APIs automatically, circumventing HTML Views
@RequestMapping("/api/items") // Base URL routing. All methods here fall under http://localhost:8081/api/items
public class ItemServerController {

    /**
     * Endpoint: GET /api/items
     * How it works: 
     * 1. Listens for incoming HTTP GET requests at the base URL.
     * 2. Returns a hardcoded List of Maps (which is Java's equivalent to a Python list of dictionaries).
     * 3. Behind the scenes, Spring Boot uses "Jackson" (a fast JSON parsing library) 
     *    to instantly convert this generic Java structure into a formatted JSON string for Python to read.
     */
    @GetMapping
    public List<Map<String, Object>> getItems() {
        return List.of(
            Map.of("id", 101, "name", "Java Spring Boot Course", "description", "Learn enterprise Java"),
            Map.of("id", 102, "name", "IntelliJ IDEA Ultimate", "description", "Best IDE for Java")
        );
    }
}
