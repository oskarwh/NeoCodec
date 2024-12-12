package se.umu.cs.pulsar;

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

import se.umu.cs.NeoPayload;

public class PClient {
    private PulsarClient client;
    private Producer<NeoPayload> producer;
    private Consumer<NeoPayload> consumer;

    public PClient(String id, String broakers) {
        try {
            this.client = PulsarClient.builder()
                .serviceUrl("pulsar://" + broakers)
                .build();
    
            this.producer = client.newProducer(Schema.PROTOBUF(NeoPayload.class))
                .topic("input-topic")
                .create();

    
            this.consumer = client.newConsumer(Schema.PROTOBUF(NeoPayload.class))
                .topic(id + "-topic")
                .subscriptionName(id + "-subscription")
                .subscriptionType(SubscriptionType.Exclusive)
                .ackTimeout(2, TimeUnit.MINUTES)
                .subscribe();
        } catch (IOException e) {
            System.err.println("Failed to initialize pulsar client: " + e.getMessage());
        }
    }

    public void send(NeoPayload file) throws IOException {
        this.producer.send(file);
    }

    /**
     * Async retrieval function for request.
     * 
     * @return
     * @throws IOException
     */
    public NeoPayload receive() throws IOException {
        Message<NeoPayload> msg = this.consumer.receive();
        return msg.getValue();
    }
}
