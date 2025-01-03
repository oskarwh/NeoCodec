package se.umu.cs;

import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

public class OutputHandlerFunction implements Function<NeoPayload, Void> {

    private static final String base = "persistent://public/neocodec/";

    @Override
    public Void process(NeoPayload data, Context context) throws Exception {       
        // Publish the message to the determined output topic
        System.out.println("----------| Puslar Function Log |----------");
        try {
            String clientTopic = Long.toString(data.getMetadata().getClientId());
            System.out.println("Sendning on Output Topic: " + clientTopic);
            context.newOutputMessage(base + clientTopic, Schema.PROTOBUF(NeoPayload.class)).value(data).send(); 
        } catch (Exception e) {
            System.out.println("Failed to send message to client topic.");
            System.out.println("-------------------------------------------");
            context.getCurrentRecord().fail();
            return null;
        }
        System.out.println("Message sent.");
        System.out.println("-------------------------------------------");
        return null;
    }    
}
