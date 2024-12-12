package se.umu.cs.pulsar;

import java.util.Collections;

import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.admin.Functions;
import org.apache.pulsar.common.functions.FunctionConfig;

public final class PulsarController {
    private static PulsarAdmin admin;
    private static int clientId = 0;

    private static final String pulsarIp = "10.43.51.255"; // Internal k3s ip
    private static final String pulsarPort = "80";

    private static final String functionFilePath = "conversion-output-handler.jar";

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

        try {
            if (!topicExists("input-topic"))
                admin.topics().createNonPartitionedTopic("input-topic");
            if (!topicExists("output-topic"))
                admin.topics().createNonPartitionedTopic("output-topic");
        } catch (PulsarAdminException e) {
            System.err.println("Failed to initalize topics");
            System.exit(-1);
        }


        Functions functions = admin.functions();

        FunctionConfig fc = new FunctionConfig();
        fc.setTenant("public");
        fc.setNamespace("default");
        fc.setName("output-handler");
        fc.setRuntime(FunctionConfig.Runtime.JAVA);
        fc.setJar(functionFilePath);
        fc.setClassName("se.umu.cs.OutputHandlerFunction");
        fc.setInputs(Collections.singleton("persistent://public/default/input-topic")); // TODO: Change to output-topic
        // fc.setParallelism(1);

        try {
            functions.createFunction(fc, functionFilePath);
        } catch (PulsarAdminException e) {
            System.err.println("Failed to create pulsar function: " + e.getMessage());
            System.exit(-1);
        }
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

    public static boolean topicExists(String topic) throws PulsarAdminException {
        int num = admin.topics().getPartitionedTopicMetadata(topic).partitions;
        if (num == 0) {
            try {
                admin.topics().getStats(topic);
            } catch (PulsarAdminException.NotFoundException e) {
                return false;
            }
        }
        return true;
    }

    public static String getBrokers() {
        return null;
    }

    public static synchronized String getNextId() {
        return String.valueOf(clientId++);
    }
}
