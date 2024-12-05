package se.umu.cs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import se.umu.cs.pulsar.clients.PAdmin;
import se.umu.cs.tui.TUI;
import se.umu.cs.workers.WorkerHandler;

public class Main {
    public static void main(String[] args) throws IOException {
        // Error logging goes to file
        File file = new File("stderr");
        FileOutputStream fos = new FileOutputStream(file);
        PrintStream ps = new PrintStream(fos);
        System.setErr(ps);

        // Get brokers
        if (args.length != 1)
            throw new IllegalArgumentException("Usage: <brokers>");
        String brokers = args[0];

        // Create admin com
        PAdmin admin = new PAdmin("localhost:8080"); // Port 8080 is Admin port for Pulsar
        
        // Create worker handler
        WorkerHandler.Initialize(brokers);

        // Create TUI
        TUI tui = new TUI();

        // Stop sequenence
        WorkerHandler.stopAll();
        admin.close();
    }
}

