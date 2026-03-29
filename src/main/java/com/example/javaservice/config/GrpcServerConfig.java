package com.example.javaservice.config;

import com.example.javaservice.grpc.server.ItemGrpcServiceImpl;
import com.example.javaservice.grpc.server.UserGrpcServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GrpcServerConfig {

    private final ItemGrpcServiceImpl itemGrpcService;
    private final UserGrpcServiceImpl userGrpcService;
    private Server server;

    public GrpcServerConfig(ItemGrpcServiceImpl itemGrpcService, UserGrpcServiceImpl userGrpcService) {
        this.itemGrpcService = itemGrpcService;
        this.userGrpcService = userGrpcService;
    }

    @PostConstruct
    public void start() throws IOException {
        // Start a dedicated thread listening on 9090 to act as our gRPC Server
        int port = 9090;
        server = ServerBuilder.forPort(port)
                .addService(itemGrpcService)
                .addService(userGrpcService)
                .build()
                .start();
        System.out.println("Java gRPC Server started, listening on " + port);
    }

    @PreDestroy
    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }
}
