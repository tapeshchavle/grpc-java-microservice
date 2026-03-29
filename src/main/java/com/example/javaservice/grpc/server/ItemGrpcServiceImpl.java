package com.example.javaservice.grpc.server;

import com.example.javaservice.grpc.EmptyRequest;
import com.example.javaservice.grpc.ItemGrpcServiceGrpc;
import com.example.javaservice.grpc.ItemListResponse;
import com.example.javaservice.grpc.ItemResponse;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

/**
 * Service: ItemGrpcServiceImpl
 * Purpose: This is the blazing-fast gRPC equivalent of an HTTP REST Controller.
 * It serves highly-compressed binary Protobuf data to any client that connects via HTTP/2 to port 9090.
 */
@Service // Registers this class as an active Spring bean so it can be picked up by our GrpcServerConfig
public class ItemGrpcServiceImpl extends ItemGrpcServiceGrpc.ItemGrpcServiceImplBase {

    /**
     * gRPC Method: getAllItems
     * How it works: 
     * 1. It is directly overriding the contract we defined in `item.proto` (via the auto-generated ImplBase).
     * 2. Parameter `request`: Contains the incoming binary payload (just EmptyRequest here).
     * 3. Parameter `responseObserver`: This is the active, open network stream back to the asking client (Python).
     */
    @Override
    public void getAllItems(EmptyRequest request, StreamObserver<ItemListResponse> responseObserver) {
        
        // 1. Build the Protobuf response using Java's Builder Pattern (standard mapping for complex Protobuf structures)
        ItemListResponse response = ItemListResponse.newBuilder()
                .addItems(ItemResponse.newBuilder().setId(101).setName("Java Spring Boot Course").setDescription("Learn enterprise Java").build())
                .addItems(ItemResponse.newBuilder().setId(102).setName("IntelliJ IDEA Ultimate").setDescription("Best IDE for Java").build())
                .build();

        // 2. Transmit the calculated binary data over the open stream back to Python
        responseObserver.onNext(response);
        
        // 3. Explicitly signal completion, telling the Python client the transaction stream handles are safe to close
        responseObserver.onCompleted();
    }
}
