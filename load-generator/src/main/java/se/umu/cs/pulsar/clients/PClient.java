package se.umu.cs.pulsar.clients;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.SubscriptionType;

public class PClient {
    private PulsarClient client;
    private Producer<byte[]> producer;
    private Consumer<byte[]> consumer;

    public PClient(String id, String broakers) {
        try {
            this.client = PulsarClient.builder()
                .serviceUrl("pulsar://" + broakers)
                .build();
    
            // TODO All clients should use same produce topic. 
            this.producer = client.newProducer(Schema.BYTES)
                .topic(id + "-producer-topic")
                .create();
    
            this.consumer = client.newConsumer()
                .topic(id + "-consumer-topic")
                .subscriptionName(id + "-subscription")
                .subscriptionType(SubscriptionType.Exclusive)
                .ackTimeout(2, TimeUnit.MINUTES)
                .subscribe();
        } catch (IOException e) {
            System.err.println("Failed to initialize pulsar client: " + e.getMessage());
        }
    }

    public void send(File file) throws IOException {
        byte[] data = new byte[(int) file.length()];
        try (FileInputStream is = new FileInputStream(file)) {
            is.read(data);
        }

        this.producer.send(data);
    }

    public void send(byte[] data) throws IOException {
        this.producer.send(data);
    }

    public void send(String filepath) throws IOException {
        this.send(new File(filepath));
    }

    /**
     * Async retrieval function for request.
     * 
     * @return
     * @throws IOException
     */
    public byte[] receive() throws IOException {
        Message<byte[]> msg = this.consumer.receive();
        return msg.getData();
    }
}
