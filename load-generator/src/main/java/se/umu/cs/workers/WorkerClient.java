package se.umu.cs.workers;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import se.umu.cs.pulsar.clients.PClient;

public class WorkerClient extends Thread {
    private String id = null;
    private PClient client = null;
    private byte[] filebuffer = null;
    private int sendRate = 0;

    Timer timer = null;

    public WorkerClient(String id, String brokers, byte[] filebuffer, int sendRate) {
        if (filebuffer == null) {
            System.err.println("Error during creation: No file provided.");
            return;
        }

        if (sendRate < 0) {
            System.err.println("Error during creation: Send rate must be >= 0.");
            return;
        }
        
        this.id = id;
        this.client = new PClient(id, brokers);
        this.filebuffer = filebuffer;
        this.sendRate = sendRate;
    }

    public synchronized void stopClient() {
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
    }

    @Override
    public void run() {
        this.timer = new Timer();
        
        TimerTask task = new TimerTask () {
            @Override
            public void run() {
                sendRequest();
            }
        };

        this.timer.scheduleAtFixedRate(task, 0l, sendRate * 1000);
    }

    private void sendRequest() {
        System.err.printf("[%s] Sending request to convert...\n", id);

        try {
            client.send(filebuffer);

            byte[] res = client.receive();

            System.err.printf("[%s] Done sending request.\n", id);
        } catch (IOException e) {
            System.err.printf("[%s] %s\n", id, e.getMessage());
        }
    }
}
