package se.umu.cs;

import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

public class OutputHandlerFunction implements Function<NeoPayload, Void> {

    @Override
    public Void process(NeoPayload data, Context context) throws Exception {
        // Publish the message to the determined output topic
        String clientTopic = String.valueOf(data.getMetadata().getClientId());
        context.newOutputMessage(clientTopic, Schema.PROTOBUF(NeoPayload.class)).value(data).send();   
        
        return null;
    }    
}
