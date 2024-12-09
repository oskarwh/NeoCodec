package se.umu.cs;

import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
// import org.apache.pulsar.client.api.PulsarClientException;

public class AdminServer {
    private final PulsarAdmin admin;
    private final GrpcServer server;
    private final int port;

    public AdminServer(int p, String pulsarUrl) {
        server = new GrpcServer(this);
        port = p;
        admin = null;
        
        // TODO: Add pulsar connection when pulsar is working in the cluster
        // try {
        //     admin = PulsarAdmin.builder()
        //         .serviceHttpUrl(pulsarUrl)
        //         .build();
        // } catch (PulsarClientException e) {
        //     System.err.println("Failed to initialize: " + e.getMessage());
        //     System.exit(-1);
        // }
    }

    public void start() {
        this.server.start(this.port);
    }

    public String createClientTopic(String ip, String port) {
        try {
            String id = ip + ":" + port + "-topic";
            admin.topics().createNonPartitionedTopic(id);
            return id;
        } catch (PulsarAdminException e) {
            System.err.println("Filed to create client topic: " + e.getMessage());
            return null;
        }
    }

    public int removeClientTopic(String topic) {
        try {
            admin.topics().delete(topic);
            return 0;
        } catch (PulsarAdminException e) {
            System.err.println("Failed to remove client topic: " + e.getMessage());
            return -1;
        }
    }
}
