package com.example.javaservice.client;

import com.example.javaservice.dto.UserDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * Service: PythonApiClient
 * Purpose: This is the actual client engine that dials outwards to our Python FastAPI microservice
 * using traditional HTTP/REST instead of gRPC.
 */
@Service
public class PythonApiClient {

    // Spring 6.1's modern synchronous HTTP Client (replaces older RestTemplate)
    private final RestClient restClient;

    public PythonApiClient() {
        // We configure the base URL centrally, so all future requests default to port 8000 (The Python Server)
        this.restClient = RestClient.builder()
                .baseUrl("http://127.0.0.1:8000")
                .build();
    }

    /**
     * Action: Fetches the entire list of Users from Python.
     * How it works internally:
     * 1. Constructs an HTTP GET request to `/api/v1/users/` (appended correctly to localhost:8000).
     * 2. `.retrieve()` actually executes the network socket operation and waits for Python to answer.
     * 3. `.body(...)` reads the raw JSON text from Python and instantly reflects it 
     *    into strict Java UserDTO instances securely using Spring's ParameterizedTypeReference.
     */
    public List<UserDTO> getAllUsers() {
        return restClient.get()
                .uri("/api/v1/users/")
                .retrieve()
                .body(new ParameterizedTypeReference<List<UserDTO>>() {});
    }

    /**
     * Action: Fetches exactly one User from Python based on their primitive ID variable.
     */
    public UserDTO getUserById(int userId) {
        return restClient.get()
                .uri("/api/v1/users/{userId}", userId)
                .retrieve()
                .body(UserDTO.class);
    }
}
