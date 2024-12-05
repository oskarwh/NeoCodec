package se.umu.cs.workers;

import java.util.HashMap;
import java.util.Map;

import se.umu.cs.pulsar.clients.PAdmin;

public final class WorkerHandler {
    private static Map<String, WorkerClient> workers = null;
    private static String brokers = null;
    private static PAdmin admin = null;

    public static void Initialize(String brokers, PAdmin admin) {
        workers = new HashMap<>();
        WorkerHandler.brokers = brokers;
        WorkerHandler.admin = admin;
    }

    public static String addClient(byte[] filebuffer, int sendRate) {
        if (brokers == null) {
            System.err.printf("[id] No brokers defined.\n");
            return null;
        }

        WorkerClient client = new WorkerClient(admin, WorkerHandler.brokers, filebuffer, sendRate);
        WorkerHandler.workers.put(client.getClientId(), client);
        return client.getClientId();
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
