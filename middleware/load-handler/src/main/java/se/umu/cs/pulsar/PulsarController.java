package se.umu.cs.pulsar;

import java.util.Collections;
import java.util.List;

import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.admin.Functions;
import org.apache.pulsar.common.policies.data.FunctionStatus;
import org.apache.pulsar.common.functions.FunctionConfig;
import org.apache.pulsar.common.policies.data.InactiveTopicPolicies;
import org.apache.pulsar.common.policies.data.InactiveTopicDeleteMode;

import se.umu.cs.NeoPayload;

import org.apache.pulsar.common.schema.SchemaInfo;
import org.apache.pulsar.common.schema.SchemaType;

public final class PulsarController {
    private static PulsarAdmin admin;
    private static int clientId = 0;

    private static final String pulsarIp = "10.43.248.237"; // Internal k3s ip
    private static final String pulsarPort = "80";

    private static final String functionFilePath = "conversion-output-handler.jar";
    
    private static final String tenant = "public";
    private static final String namespace = "neocodec"; 
    private static final String inOutNamespace = "inout";
    private static final String base = "persistent://" + tenant + "/" + namespace + "/";
    private static final String inOutBase = "persistent://" + tenant + "/" + inOutNamespace + "/";

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

        si = SchemaInfo.builder()
            .name("NeoPayload")
            .type(SchemaType.PROTOBUF)
            .schema(NeoPayload.getDescriptor().toProto().toByteArray())
            .build();

        System.out.println("configure pulsar");
        configurePulsar(true);

        // Remove function if it exists
        FunctionConfig fc = null;
        try {
            fc = admin.functions().getFunction(tenant, inOutNamespace, "output-handler");
            if (fc != null) {
                admin.functions().deleteFunction(tenant, inOutNamespace, "output-handler");
            }
        } catch (PulsarAdminException e) {
            System.err.println("Failed to delete pulsar function: " + e.getMessage());
        }

        // Create new pulsar function
        Functions functions = admin.functions();
        fc = new FunctionConfig();
        fc.setTenant(tenant);
        fc.setNamespace(inOutNamespace);
        fc.setName("output-handler");
        fc.setRuntime(FunctionConfig.Runtime.JAVA);
        fc.setJar(functionFilePath);
        fc.setClassName("se.umu.cs.OutputHandlerFunction");
        fc.setInputs(Collections.singleton("input-topic")); // TODO: Change to output-topic
        fc.setParallelism(1);

        try {
            functions.createFunction(fc, functionFilePath);
        } catch (PulsarAdminException e) {
            System.err.println("Failed to create pulsar function: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(-1);
        }

        // try {
        //     Thread.sleep(5000); // Wait for function to start
        // } catch(Exception e) {
        //     System.err.println("Failed to sleep: " + e.getMessage());
        //     System.exit(-1);
        // }

        // Check function status
        try {    
            fc = null;
            fc = admin.functions().getFunction(tenant, inOutNamespace, "output-handler");
            if (fc == null){
                System.err.println("Function is not created.");
                System.exit(-1);
            }
            // Check if function is running
            FunctionStatus status = functions.getFunctionStatus(tenant, inOutNamespace, "output-handler");
            if (status.getNumInstances() == 0) {
                System.err.println("Function is not found. Exiting...");
                System.exit(-1);
            }
            if (status.getNumRunning() == 0) {
                System.err.println("Function is not running. Exiting...");
                System.exit(-1);
            }
        } catch(PulsarAdminException e) {
            System.err.println("Failed to check function status: " + e.getMessage());
            System.exit(-1);
        }
    }

    private static void configurePulsar(boolean cleanOld){
         try {
            if (!namespaceExists(namespace)) // For Client Topics
                admin.namespaces().createNamespace(tenant + "/" + namespace);
            if (!namespaceExists(inOutNamespace)) // For general topics
                admin.namespaces().createNamespace(tenant + "/" + inOutNamespace);

            if (!topicExists(inOutBase, "input-topic")) {
                admin.topics().createNonPartitionedTopic(inOutBase + "input-topic");
                admin.schemas().createSchema(inOutBase + "input-topic", si);
            }
            if (!topicExists(inOutBase, "output-topic")) {
                admin.topics().createNonPartitionedTopic(inOutBase + "output-topic");
                admin.schemas().createSchema(inOutBase + "output-topic", si);
            }

            InactiveTopicPolicies policies = new InactiveTopicPolicies(
                InactiveTopicDeleteMode.delete_when_no_subscriptions,
                0,
                false
            );
            
            admin.namespaces().setInactiveTopicPolicies(tenant + "/" + inOutNamespace, policies);
        } catch (PulsarAdminException e) {
            System.err.println("Failed to initalize topics: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(-1);
        }
        
        if(cleanOld) {
            try {
                // Kill all previous client topics
                List<String> topics = admin.topics().getList(tenant + "/" + namespace);
                
                // Delete each topic
                for (String topic : topics) {
                    admin.topics().delete(topic, true);
                    System.out.println("Deleted old client topic: " + topic);
                }

            }catch (PulsarAdminException e) {
                System.err.println("Failed to clear old client topics.");
                e.printStackTrace();
                System.exit(-1);
            }
            System.out.println("Old topics cleaned up.");
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

    public static boolean topicExists(String base, String topic) {
        try {
            int num = admin.topics().getPartitionedTopicMetadata(base + topic).partitions;
            if (num == 0)
                admin.topics().getStats(base + topic);
            else
                return false;
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
