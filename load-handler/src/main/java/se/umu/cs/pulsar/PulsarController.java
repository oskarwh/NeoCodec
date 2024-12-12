package se.umu.cs.pulsar;

import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.PulsarClientException;

public final class PulsarController {
    private static PulsarAdmin admin;
    private static int clientId = 0;

    private static final String pulsarIp = "10.43.51.255"; // Internal k3s ip
    private static final String pulsarPort = "80";

    public static void init() {
        // port = p;
        // admin = null;
        
        try {
            String url = "http://" + pulsarIp + ":" + pulsarPort;
            admin = PulsarAdmin.builder()
                .serviceHttpUrl(url)
                .build();
            System.out.println("Pulsar Admin created.");
        } catch (PulsarClientException e) {
            System.err.println("Failed to initialize: " + e.getMessage());
            System.exit(-1);
        }

        // System.out.println("Befreo");
        // createClientTopic("1", "1");
        // System.out.println("after");
    }

    public static String createClientTopic(String id) {
        try {
            admin.topics().createNonPartitionedTopic(id);
            return id;
        } catch (PulsarAdminException e) {
            System.err.println("Filed to create client topic: " + e.getMessage());
            return null;
        }
    }

    public static int removeClientTopic(String topic) {
        try {
            admin.topics().delete(topic);
            return 0;
        } catch (PulsarAdminException e) {
            System.err.println("Failed to remove client topic: " + e.getMessage());
            return -1;
        }
    }

    public static String getBrokers() {
        return null;
    }

    public static synchronized String getNextId() {
        return String.valueOf(clientId++);
    }
}
