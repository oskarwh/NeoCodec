package se.umu.cs;

import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

public class OutputHandlerFunction implements Function<NeoFile, Void> {

    @Override
    public Void process(NeoFile data, Context context) throws Exception {
        // Publish the message to the determined output topic
        context.newOutputMessage(data.getOutputTopic(), Schema.PROTOBUF(NeoFile.class)).value(data).sendAsync();   
        return null;
    }    
}
