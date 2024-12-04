package se.umu.cs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.concurrent.TimeUnit;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.SubscriptionType;

import com.google.protobuf.ByteString;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

public class Converter {
    /**
     * args[0] = Comma separated list of brokers
     */  
    public static void main(String[] args) throws IOException {
        String brokers = args[0];

        if (brokers == null || brokers.isEmpty())
            throw new IllegalArgumentException("Usage: java -jar converter.jar <brokers>");
        
        PulsarClient client = PulsarClient.builder()
            .serviceUrl("pulsar://" + brokers)
            .build();

        
        Consumer<byte[]> consumer = client.newConsumer()
            .topic("input-topic")
            .subscriptionName("input-subscription")
            .subscriptionType(SubscriptionType.Shared)
            .ackTimeout(30, TimeUnit.SECONDS)
            .subscribe();
        
        while (true) {
            Message<byte[]> msg = consumer.receive();

            try {
                ProtoPayload vm = Serializer.deserialize(msg.getData());

                // Create temporary file 
                File tempFile;
                if (vm.getSourceType().charAt(0) == '.')
                    tempFile = File.createTempFile("input", vm.getSourceType());
                else
                    tempFile = File.createTempFile(vm.getSource(), "." + vm.getSourceType());

                // Write data to temporary file
                try (FileOutputStream os = new FileOutputStream(tempFile)) {
                    os.write(vm.getData().toByteArray());
                }
                
                // Convert file and replace double dots with single dots
                String output = vm.getTarget() + "." + vm.getTargetType().replace("..", ".");
                convert(tempFile.getAbsolutePath(), output);

                // Create output payload
                File of = new File(output);
                byte[] byteArray = new byte[(int) of.length()];
                try (FileInputStream is = new FileInputStream(of)) {
                    is.read(byteArray);
                }
                
                ByteString dataByteString = ByteString.copyFrom(byteArray);
                ProtoPayload.Builder vmBuilder = ProtoPayload.newBuilder()
                    .setSource(vm.getSource())
                    .setSourceType(vm.getTargetType())
                    .setTarget(vm.getSource())
                    .setTargetType(vm.getSourceType())
                    .setData(dataByteString)
                    .setIndex(vm.getIndex());

                // Serialize and send output payload
                byte[] outputPayload = Serializer.serialize(vmBuilder.build());

                // Send output payload to next topic
                Producer<byte[]> producer = client.newProducer(Schema.BYTES)
                    .topic("output-topic")
                    .create();

                producer.newMessage()
                    .value(outputPayload)
                    .sendAsync()
                    .thenAccept(mId -> {
                        System.out.println("Message " + mId + " was succefully delivered.");
                    });

                consumer.acknowledge(msg);
            } catch (Exception e) {
                System.err.println("Failed to convert file: " + e.getMessage());
                consumer.negativeAcknowledge(msg);
            }
        }
    }
        
    public static void convert(String source, String target) throws Exception {
        File f = new File(source);
        if (f.exists() && !f.isDirectory())
            throw new IllegalArgumentException("Input file does not exist, or is a directory.");

        FFmpeg ffmpeg = new FFmpeg("/usr/bin/ffmpeg");
        FFprobe ffprobe = new FFprobe("/usr/bin/ffprobe");

        FFmpegBuilder builder = new FFmpegBuilder()
            .setInput(source)
            .overrideOutputFiles(true)
            .addOutput(target)
                .setFormat(target.substring(target.indexOf('.') + 1))
                .setAudioCodec("aac")
                .setVideoCodec("libx264")
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
    }
}