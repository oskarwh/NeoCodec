package se.umu.cs;

import java.util.Map;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

public class HazelcastHandler {
    private final String MAP_NAME = "NeocodecErrorMap"; 
    private HazelcastInstance client;

    public HazelcastHandler() {
        ClientConfig config = new ClientConfig();
        config.getNetworkConfig().setSmartRouting(false);
        config.setClusterName("NeoCodec");
        config.getNetworkConfig().addAddress("hz-hazelcast");

        this.client = HazelcastClient.newHazelcastClient(config);
    }
    

    public boolean checkError(String id){
        Map<Integer, String> errorMap = this.client.getMap(MAP_NAME);
        boolean ret = (errorMap.get(id) == null);
        return ret;
    }

}