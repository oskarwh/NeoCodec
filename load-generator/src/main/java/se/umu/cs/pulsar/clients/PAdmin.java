package se.umu.cs.pulsar.clients;

import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.PulsarClientException;

import se.umu.cs.pulsar.interfaces.PulsarAdminCommunication;

public class PAdmin implements PulsarAdminCommunication{
    private PulsarAdmin admin;

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
    public String createClientTopic(String ip, String port){

        return null;
    }

    @Override
    public int removeClientTopic(String ip, String port){

        return 0;
    }

    public void close() {
        this.admin.close();
    }
}