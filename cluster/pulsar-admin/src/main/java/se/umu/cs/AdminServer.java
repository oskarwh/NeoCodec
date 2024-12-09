package se.umu.cs;

import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;

public class AdminServer {
    private PulsarAdmin admin;
    private GrpcServer server;

    public AdminServer(int port, String pulsarUrl) {
        try {
            admin = PulsarAdmin.builder()
                .serviceHttpUrl(pulsarUrl)
                .build();

            this.server = GrpcServer(this);
            this.server.start(port);

        } catch (Exception e) {

        }
    }

    public String createClientTopic(String ip, String port) {
        try {
            String id = ip + ":" + port + "-topic";
            admin.topics().createNonPartitionedTopic(id);
            return id;
        } catch (PulsarAdminException e) {

            return null;
        }
    }

    public int removeClientTopic(String topic) {
        try {
            admin.topics().delete(topic);
            return 0;
        } catch (PulsarAdminException e) {
            System.err.println("Failed to remove client: " + e.getMessage());
            return -1;
        }
    }
}
