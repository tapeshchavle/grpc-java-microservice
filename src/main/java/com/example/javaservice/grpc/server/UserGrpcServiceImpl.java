package com.example.javaservice.grpc.server;

import com.example.javaservice.grpc.user.EmptyUserRequest;
import com.example.javaservice.grpc.user.UserGrpcServiceGrpc;
import com.example.javaservice.grpc.user.UserListResponse;
import com.example.javaservice.grpc.user.UserResponse;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

/**
 * Service: UserGrpcServiceImpl
 * Purpose: Binds the local Java Data onto the heavily-typed gRPC socket handlers.
 * Clients using HTTP/2 protocol bufffers (like our Python Service) reach here directly.
 */
@Service // Injects into our GrpcServerConfig engine
public class UserGrpcServiceImpl extends UserGrpcServiceGrpc.UserGrpcServiceImplBase {

    /**
     * gRPC Method: getAllUsers
     * How it behaves internally: 
     * 1. This method executes on a dedicated network streaming thread supplied by Netty (underlying server).
     * 2. `responseObserver` lets us push multiple chunks of binary data (onNext) before formally sealing the network connection (onCompleted).
     */
    @Override
    public void getAllUsers(EmptyUserRequest request, StreamObserver<UserListResponse> responseObserver) {
        
        // Use auto-generated Builders to statically construct the User Protobuf instances in memory
        UserListResponse response = UserListResponse.newBuilder()
                .addUsers(UserResponse.newBuilder().setId(1).setUsername("java_dev").setEmail("dev@java.com").build())
                .addUsers(UserResponse.newBuilder().setId(2).setUsername("spring_master").setEmail("master@spring.io").build())
                .build();

        // Emits the computed binary packet downstream to the requester 
        responseObserver.onNext(response);
        
        // Required API behavior dictating that the sequence is permanently completed.
        responseObserver.onCompleted();
    }
}
