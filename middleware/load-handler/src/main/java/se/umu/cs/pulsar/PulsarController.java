package se.umu.cs.pulsar;

import java.util.Collections;

import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.admin.Functions;
import org.apache.pulsar.common.functions.FunctionConfig;

import se.umu.cs.NeoPayload;

import org.apache.pulsar.common.schema.SchemaInfo;
import org.apache.pulsar.common.schema.SchemaType;

public final class PulsarController {
    private static PulsarAdmin admin;
    private static int clientId = 0;

    private static final String pulsarIp = "10.43.51.255"; // Internal k3s ip
    private static final String pulsarPort = "80";

    private static final String functionFilePath = "conversion-output-handler.jar";
    
    private static final String tenant = "public";
    private static final String namespace = "neocodec"; 
    private static final String base = "persistent://" + tenant + "/" + namespace + "/";

    private static SchemaInfo si = null;

    public static void init() {
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

        System.out.println(NeoPayload.getDescriptor().toProto());

        si = SchemaInfo.builder()
            .name("NeoPayload")
            .type(SchemaType.PROTOBUF)
            .schema(NeoPayload.getDescriptor().toProto().toByteArray())
            .build();





        try {
            if (!namespaceExists(namespace))
                admin.namespaces().createNamespace(tenant + "/" + namespace);

            if (!topicExists("input-topic")) {
                admin.topics().createNonPartitionedTopic(base + "input-topic");
                admin.schemas().createSchema(base + "input-topic", si);
            }
            if (!topicExists("output-topic")) {
                admin.topics().createNonPartitionedTopic(base + "output-topic");
                admin.schemas().createSchema(base + "output-topic", si);
            }
        } catch (PulsarAdminException e) {
            System.err.println("Failed to initalize topics: " + e.getMessage());
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
        fc.setInputs(Collections.singleton(base + "input-topic")); // TODO: Change to output-topic

        try {
            functions.createFunction(fc, functionFilePath);
        } catch (PulsarAdminException e) {
            System.err.println("Failed to create pulsar function: " + e.getMessage());
            System.exit(-1);
        }
    }

    public static String createClientTopic(String id) {
        try {
            admin.topics().createNonPartitionedTopic(base + id);
            return id;
        } catch (PulsarAdminException e) {
            System.err.println("Filed to create client topic: " + e.getMessage());
            return null;
        }
    }

    public static int removeClientTopic(String topic) {
        try {
            admin.topics().delete(base + topic);
            return 0;
        } catch (PulsarAdminException e) {
            System.err.println("Failed to remove client topic: " + e.getMessage());
            return -1;
        }
    }

    public static boolean topicExists(String topic) {
        try {
            int num = admin.topics().getPartitionedTopicMetadata(base + topic).partitions;
            if (num == 0)
                admin.topics().getStats(base + topic);
        } catch (PulsarAdminException e) {
            return false;
        }
        return true;
    }

    public static boolean namespaceExists(String namespace) throws PulsarAdminException {
        try {
            admin.namespaces().getPolicies(tenant + "/" + namespace);
        } catch (PulsarAdminException.NotFoundException e) {
            return false;
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
