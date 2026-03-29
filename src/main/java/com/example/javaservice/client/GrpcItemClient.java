package com.example.javaservice.client;

import com.example.javaservice.grpc.EmptyRequest;
import com.example.javaservice.grpc.GetItemRequest;
import com.example.javaservice.grpc.ItemGrpcServiceGrpc;
import com.example.javaservice.grpc.ItemListResponse;
import com.example.javaservice.grpc.ItemResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PreDestroy;

/**
 * Service: GrpcItemClient
 * Purpose: This acts as a blazing-fast network client to connect directly to 
 * the Python server's dedicated gRPC secondary port (50051). 
 */
@Service
public class GrpcItemClient {

    // ManagedChannel represents and manages the literal HTTP/2 active socket connection pool underlying gRPC
    private final ManagedChannel channel;
    
    // The "Stub" is the auto-generated boilerplate code that totally hides the complexities of byte-buffer networking 
    private final ItemGrpcServiceGrpc.ItemGrpcServiceBlockingStub blockingStub;

    public GrpcItemClient() {
        // 1. Open a persistent tunnel directly to Python gRPC server running on 50051
        this.channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext() // Skips SSL/TLS certificate checking, which is required for local localhost development
                .build();
                
        // 2. Initialize a "Blocking Stub" (meaning that when this thread makes a call, Java will pause and wait synchronously for Python's answer)
        this.blockingStub = ItemGrpcServiceGrpc.newBlockingStub(channel);
    }

    /**
     * Action: Fetches all items via backend channels.
     * Internal: Synthesizes an EmptyRequest binary packet, pushes it down the HTTP/2 stream, and Python streams back an ItemListResponse Protobuf object.
     */
    public ItemListResponse getAllItems() {
        return blockingStub.getAllItems(EmptyRequest.newBuilder().build());
    }

    /**
     * Action: Instantiates a GetItemRequest object, encodes the ID into binary, and blasts it to Python.
     */
    public ItemResponse getItem(int id) {
        return blockingStub.getItem(GetItemRequest.newBuilder().setId(id).build());
    }
    
    /**
     * @PreDestroy ensures that when Spring Boot gracefully exits, it politely closes the open 
     * HTTP/2 socket connections to Python so CPU threads and memory resources aren't leaked.
     */
    @PreDestroy
    public void shutdown() {
        if (channel != null) {
            channel.shutdown();
        }
    }
}
