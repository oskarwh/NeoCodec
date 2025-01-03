package se.umu.cs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.concurrent.TimeUnit;

import java.lang.StringBuilder;

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
    private static final String PULSAR_BROKERS = "pulsar://10.43.101.106:6650";
    
    public static void main(String[] args) throws IOException {
        PulsarClient client = PulsarClient.builder()
            .serviceUrl(PULSAR_BROKERS)
            .build();

        
        Consumer<NeoPayload> consumer = client.newConsumer(Schema.PROTOBUF(NeoPayload.class))
            .topic("persistent://public/inout/input-topic")
            .subscriptionName("input-subscription")
            .subscriptionType(SubscriptionType.Shared)
            .ackTimeout(30, TimeUnit.SECONDS)
            .subscribe();
        
        Producer<NeoPayload> producer = client.newProducer(Schema.PROTOBUF(NeoPayload.class))
                .topic("persistent://public/inout/output-topic")
                .create();

        while (true) {
            Message<NeoPayload> msg = consumer.receive();

            System.out.println("----------| Starting convertion |----------");
            
            NeoPayload result;

            try {
                NeoFile file = msg.getValue().getFile();
                // String sourceType = "." + new StringBuilder(new StringBuilder(file.getFileName()).reverse().toString().split(".")[0]).reverse().toString();
                String sourceType = file.getFileName().substring(file.getFileName().lastIndexOf('.'));
                String targetType = "." + file.getTargetType().getValueDescriptor().getName();
                System.out.println("Convertion from type " + sourceType + " to " + targetType + " requested.");

                // Create temporary file 
                File tempFile;
                tempFile = File.createTempFile("input", sourceType);

                System.out.println("Temp file created: " + tempFile.getAbsolutePath());

                // Write data to temporary file
                try (FileOutputStream os = new FileOutputStream(tempFile)) {
                    os.write(file.getFile().toByteArray());
                    os.flush();
                }

                System.out.println("Data written to temp file.");
                
                // Convert file and replace double dots with single dots
                String output = "output" + targetType;
                convert(tempFile.getAbsolutePath(), output);

                System.out.println("File converted to: " + output);

                // Create output payload
                File of = new File(output);
                byte[] byteArray = new byte[(int) of.length()];
                try (FileInputStream is = new FileInputStream(of)) {
                    is.read(byteArray);
                }

                System.out.println("Data read from file.");
                
                ByteString dataByteString = ByteString.copyFrom(byteArray);
                result = NeoPayload.newBuilder()
                    .setMetadata(msg.getValue().getMetadata())
                    .setFile(NeoFile.newBuilder()
                        .setTargetType(file.getTargetType())
                        .setFile(dataByteString)
                        .build())
                    .build();

                // TODO: For testing purposes, result will be thrown
                System.out.println("Successfull convertion of file: " + file.getFileName() + " to " + file.getTargetType().getValueDescriptor().getName() + " format.");
            } catch (Exception e) {
                System.err.println("FFmpeg failed to convert file, please check your input file for clues: " + e.getMessage());
                
                result = NeoPayload.newBuilder()
                    .setMetadata(NeoMetadata.newBuilder()
                        .setClientId(msg.getValue().getMetadata().getClientId())
                        .setError(-1)
                        .setErrorMessage(e.getMessage())
                        .build())
                    .build();
            }
            
            consumer.acknowledge(msg);
            
            // Send output payload to next topic
            producer.newMessage()
                .value(result)
                .sendAsync()
                .thenAccept(mId -> {
                    System.out.println("Message " + mId + " was succefully delivered.");
            });
            
            System.out.println("----------| Convertion done |----------");
        }
    }
        
    public static void convert(String source, String target) throws Exception {
        File f = new File(source);
        if (!f.exists() || f.isDirectory())
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