package se.umu.cs;

import java.io.IOException;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.stub.StreamObserver;

public class GrpcServer {
    private Server server;
    private final AdminServer handler;

    public GrpcServer(AdminServer h) {
        handler = h;
    }

    public void start(int port) {
        try {
            System.out.println("Starting gRPC server...");
            server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
                .addService(new PulsarComService(handler))
                .build()
                .start();
                
            System.out.println("gRPC server running...");
            server.awaitTermination();
        } catch (IllegalStateException | IOException | InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(-1);
        }
    }

    public void stop() {
        this.server.shutdown();
    }

    public class PulsarComService extends PulsarComServiceGrpc.PulsarComServiceImplBase {
        private final AdminServer handler;
        
        public PulsarComService(AdminServer h) {
            handler = h;
        }

        @Override
        public void createTopic(RpcAddress address, StreamObserver<RpcId> responseObserver) {
            String id = handler.createClientTopic(address.getIp(), address.getPort());

            String error = null;        
            if (id == null)
                error = "Failed to create topic.";
            
            RpcId res = RpcId.newBuilder()
                .setId(id)
                .setError(error)
                .build();

            responseObserver.onNext(res);
            responseObserver.onCompleted();
        }

        @Override
        public void destroyTopic(RpcId id, StreamObserver<RpcEmpty> responseObserver) {
            int status = handler.removeClientTopic(id.getId());

            String error = null;
            if (status != 0)
                error = "Failed to remove topic.";

            RpcEmpty res = RpcEmpty.newBuilder()
                .setError(error)
                .build();
                
            responseObserver.onNext(res);
            responseObserver.onCompleted();
        }
    }
}