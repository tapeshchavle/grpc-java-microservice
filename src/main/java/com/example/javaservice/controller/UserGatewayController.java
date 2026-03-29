package com.example.javaservice.controller;

import com.example.javaservice.client.PythonApiClient;
import com.example.javaservice.dto.UserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller: UserGatewayController
 * Purpose: This controller sits inside the Java process but doesn't actually have a local database.
 * Instead, it acts as an API "Gateway" or "Reverse Proxy". It intercepts a request from your web browser, 
 * turns around, blindly queries the Python microservice using HTTP/REST, and hands Python's answer back to your browser.
 */
@RestController
@RequestMapping("/java-api/users")
public class UserGatewayController {

    // Spring Boot "Dependency Injection": Asks the Spring Context to provide the singleton instance of PythonApiClient.
    private final PythonApiClient pythonApiClient;

    public UserGatewayController(PythonApiClient pythonApiClient) {
        this.pythonApiClient = pythonApiClient;
    }

    /**
     * Endpoint: GET /java-api/users
     * Action: 
     * 1. Hitting this endpoint in your browser pauses the Java thread. 
     * 2. `pythonApiClient` securely fires a completely new HTTP call over to Python's port 8000.
     * 3. Python computes the answer and returns it as a string to Java.
     * 4. Java reads it into `List<UserDTO>` and then forwards it to your browser.
     */
    @GetMapping
    public List<UserDTO> fetchUsersFromPython() {
        return pythonApiClient.getAllUsers();
    }

    /**
     * A simple health-check or testing route.
     */
    @GetMapping("/hi")
    public String hi(){
        return "Hello Tapesh";
    }

    /**
     * Endpoint: GET /java-api/users/{id}
     * `@PathVariable` dynamically slices the '{id}' section out of the URL (e.g. /java-api/users/2)
     * and injects it into the integer `id` variable below.
     */
    @GetMapping("/{id}")
    public UserDTO fetchSingleUserFromPython(@PathVariable int id) {
        return pythonApiClient.getUserById(id);
    }
}
