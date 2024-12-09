package se.umu.cs;

public class GrpcServer {
    private Server server;
    protected AdminServer handler;

    public GrpcServer(AdminServer handler) {
        this.handler = handler;
    }

    public void start(int port) {
        try {
            server = Grpc.newServerBuilderForPort(port, InsecureSErverCredentials.create())
                .addService(new PulsarComService)
                .build()
                .start();
                
            server.awaitTermination();
        } catch (IllegalStateException e) {
            
        } catch (IOException e) {

        }
    }

    public void stop() {
        try {
            this.server.shutdown();
        } catch (InterruptedExcetpion e) {

        }
    }

    public class PulsarComService extends RpcServiceGrpc.RpcServiceImplBase {
        public PulsarComService() {}

        @Override
        public void createTopic(RpcAddress address, StreamObserver<RpcId> responseObserver) {
            handler.createClientTopic(address.getIp(), address.getPort());
        }

        @Override
        public void destoryTopic(RpcId id, StreamObserver<RpcEmpty> responseObserver) {
            handler.removeClientTopic(id.getId());
        }
    }
}