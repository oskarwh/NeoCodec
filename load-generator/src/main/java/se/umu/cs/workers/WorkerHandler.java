package se.umu.cs.workers;

import java.util.HashMap;
import java.util.Map;

public final class WorkerHandler {
    private static Map<String, WorkerClient> workers = null;
    private static String brokers = null;

    public static void Initialize(String brokers) {
        workers = new HashMap<>();
        WorkerHandler.brokers = brokers;
    }

    public static void addClient(String id, byte[] filebuffer, int sendRate) {
        if (brokers == null) {
            System.err.printf("[id] No brokers defined.\n");
            return;
        }

        WorkerClient client = new WorkerClient(id, WorkerHandler.brokers, filebuffer, sendRate);
        WorkerHandler.workers.put(id, client);
    }

    public static void startClient(String id) {
        WorkerClient client = WorkerHandler.workers.get(id);
        client.start();
    }

    public static void stopClient(String id) {
        WorkerClient client = WorkerHandler.workers.get(id);
        client.stopClient();
    }

    public static void stopAll() {
        for (String key : workers.keySet()) {
            stopClient(key);
        }
    }
}
