package se.umu.cs.workers;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import se.umu.cs.pulsar.clients.PClient;
import se.umu.cs.pulsar.clients.PAdmin;

public class WorkerClient extends Thread {
    private String id = null;
    private PClient client = null;
    private byte[] filebuffer = null;
    private int sendRate = 0;

    private PAdmin admin = null;

    Timer timer = null;

    public WorkerClient(PAdmin admin, String brokers, byte[] filebuffer, int sendRate) {
        if (filebuffer == null) {
            System.err.println("Error during creation: No file provided.");
            return;
        }

        if (sendRate < 0) {
            System.err.println("Error during creation: Send rate must be >= 0.");
            return;
        }
        
        String ip;
        try {
            try(final DatagramSocket socket = new DatagramSocket()){
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                ip = socket.getLocalAddress().getHostAddress();
            }
        } catch (UnknownHostException | SocketException e) {
            System.err.println("Failed to fetch ip: " + e.getMessage());
            return;
        }

        this.id = admin.createClientTopic(ip);
        this.client = new PClient(id, brokers);
        this.filebuffer = filebuffer;
        this.sendRate = sendRate;
        this.admin = admin;
    }

    public synchronized void killClient() {
        stopClient();
        this.admin.removeClientTopic(this.id);
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

    public String getClientId() {
        return this.id;
    }
}
