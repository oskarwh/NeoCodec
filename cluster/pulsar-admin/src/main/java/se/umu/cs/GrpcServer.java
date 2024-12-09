package se.umu.cs;

import java.io.IOException;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.stub.StreamObserver;

public class GrpcServer {
    private Server server;
    private AdminServer handler;

    public GrpcServer(AdminServer handler) {
        this.handler = handler;
    }

    public void start(int port) {
        try {
            server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
                .addService(new PulsarComService(this.handler))
                .build()
                .start();
                
            server.awaitTermination();
        } catch (IllegalStateException | IOException | InterruptedException e) {
            
        }
    }

    public void stop() {
        this.server.shutdown();
    }

    public class PulsarComService extends PulsarComServiceGrpc.PulsarComServiceImplBase {
        private AdminServer handler;
        
        public PulsarComService(AdminServer handler) {
            this.handler = handler;
        }

        @Override
        public void createTopic(RpcAddress address, StreamObserver<RpcId> responseObserver) {
            handler.createClientTopic(address.getIp(), address.getPort());
        }

        @Override
        public void destroyTopic(RpcId id, StreamObserver<RpcEmpty> responseObserver) {
            handler.removeClientTopic(id.getId());
        }
    }
}