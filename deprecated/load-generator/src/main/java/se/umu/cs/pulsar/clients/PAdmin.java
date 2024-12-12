package se.umu.cs.pulsar.clients;

import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;

import se.umu.cs.pulsar.interfaces.PulsarAdminCommunication;

public class PAdmin implements PulsarAdminCommunication{
    private PulsarAdmin admin;
    private int clientId = 0;

    public PAdmin(String pulsar_url) {
        try {
            // Create PulsarAdmin client
            this.admin = PulsarAdmin.builder()
                    .serviceHttpUrl(pulsar_url)
                    .build();

            System.err.println("Pulsar Admin connected!");

        } catch (Exception e) {
            System.err.println("Error | Pulsar Admin : " + e.getMessage());
        }
    }

    @Override
    public String createClientTopic(String ip) {
        try {
            String id = ip + ":" + getNextId();
            admin.topics().createNonPartitionedTopic(id);
            return id;
        } catch (PulsarAdminException e) {
            System.err.println("Failed to create topic: " + e.getMessage());
            return null;
        }
    }

    @Override
    public int removeClientTopic(String topic) {
        try {
            this.admin.topics().delete(topic);
            return 0;
        } catch (PulsarAdminException e) {
            System.err.println("Failed to remove client: " + e.getMessage());
            return -1;
        }
    }

    public void close() {
        this.admin.close();
    }

    private String getNextId() {
        return String.valueOf(clientId++);
    }
}