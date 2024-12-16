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
    private static final String PULSAR_BROKERS = "pulsar://10.43.51.255:6650";
    
    public static void main(String[] args) throws IOException {
        PulsarClient client = PulsarClient.builder()
            .serviceUrl(PULSAR_BROKERS)
            .build();

        
        Consumer<NeoPayload> consumer = client.newConsumer(Schema.PROTOBUF(NeoPayload.class))
            .topic("input-topic")
            .subscriptionName("input-subscription")
            .subscriptionType(SubscriptionType.Shared)
            .ackTimeout(30, TimeUnit.SECONDS)
            .subscribe();
        
        while (true) {
            Message<NeoPayload> msg = consumer.receive();

            try {
                NeoFile file = msg.getValue().getFile();
                String sourceType = "." + file.getFileName().reverse().split(".")[0].reverse();
                String targetType = "." + file.getTargetType().getValueDescriptor().getName();

                // Create temporary file 
                File tempFile;
                tempFile = File.createTempFile("input", sourceType);

                // Write data to temporary file
                try (FileOutputStream os = new FileOutputStream(tempFile)) {
                    os.write(file.getFile().toByteArray());
                }
                
                // Convert file and replace double dots with single dots
                String output = "output" + targetType;
                convert(tempFile.getAbsolutePath(), output);

                // Create output payload
                File of = new File(output);
                byte[] byteArray = new byte[(int) of.length()];
                try (FileInputStream is = new FileInputStream(of)) {
                    is.read(byteArray);
                }
                
                ByteString dataByteString = ByteString.copyFrom(byteArray);
                NeoPayload outputPayload = NeoPayload.newBuilder()
                    .setMetadata(msg.getValue().getMetadata())
                    .setFile(NeoFile.newBuilder()
                        .setSourceType(file.getTargetType())
                        .setTargetType(file.getTargetType())
                        .setFile(dataByteString)
                        .build())
                    .build();

                // Send output payload to next topic
                Producer<NeoPayload> producer = client.newProducer(Schema.PROTOBUF(NeoPayload.class))
                    .topic("output-topic")
                    .create();

                producer.newMessage()
                    .value(outputPayload)
                    .sendAsync()
                    .thenAccept(mId -> {
                        System.out.println("Message " + mId + " was succefully delivered.");
                    });

                producer.close();

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