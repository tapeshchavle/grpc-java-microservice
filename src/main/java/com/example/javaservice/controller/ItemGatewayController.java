package com.example.javaservice.controller;

import com.example.javaservice.client.GrpcItemClient;
import com.example.javaservice.grpc.ItemListResponse;
import com.example.javaservice.grpc.ItemResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller: ItemGatewayController
 * Purpose: A REST Controller that exposes our robust binary gRPC capabilities to a standard Web Browser.
 * Web browsers cannot speak binary Protobuf natively easily, so this acts as the translator.
 */
@RestController
@RequestMapping("/java-api/items")
public class ItemGatewayController {

    private final GrpcItemClient grpcItemClient;

    public ItemGatewayController(GrpcItemClient grpcItemClient) {
        this.grpcItemClient = grpcItemClient;
    }

    /**
     * Endpoint: GET /java-api/items
     * Internal Sequence:
     * 1. HTTP/1.1 REST request arrives from Browser.
     * 2. `grpcItemClient` shifts gears and opens an HTTP/2 Binary Socket to Python (port 50051).
     * 3. Evaluates Python's response (`ItemListResponse`), mapping the native objects directly to 
     *    `java.util.Map` using Java Streams so the browser receives standard formatted JSON.
     */
    @GetMapping
    public List<Map<String, Object>> getAllItems() {
        // High-speed Protobuf Call
        ItemListResponse response = grpcItemClient.getAllItems();
        
        // Translation layer parsing Protobuf to JSON Dictionaries
        return response.getItemsList().stream().map(item -> Map.<String, Object>of(
                "id", item.getId(),
                "name", item.getName(),
                "description", item.getDescription()
        )).collect(Collectors.toList());
    }

    /**
     * Endpoint: GET /java-api/items/{id}
     */
    @GetMapping("/{id}")
    public Map<String, Object> getItem(@PathVariable int id) {
        ItemResponse item = grpcItemClient.getItem(id);
        
        return Map.of(
                "id", item.getId(),
                "name", item.getName(),
                "description", item.getDescription()
        );
    }
}
