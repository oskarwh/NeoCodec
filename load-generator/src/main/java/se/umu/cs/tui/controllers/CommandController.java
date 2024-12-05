package se.umu.cs.tui.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import se.umu.cs.workers.WorkerHandler;
import se.umu.cs.tui.windows.ControllWindow;
import se.umu.cs.tui.windows.OutputWindow;

public class CommandController {
    private ControllWindow controllWindow = null;
    private OutputWindow outputWindow = null;
    
    public CommandController() { }

    public CommandController(ControllWindow cw, OutputWindow ow) {
        this.controllWindow = cw;
        this.outputWindow = ow;
    }

    public void handleCommand(String command) throws IOException {
        // Handle the command
        System.err.println("Command: " + command);

        String[] words = command.split(" ");
        switch (words[0]) {
            case "run":
                runCommand(command);
                break;

            case "clients":
                if (words.length != 2) {
                    System.err.println("Usage: clients <number>");
                    break;
                }
                int numClients = Integer.parseInt(words[1]);
                setClients(numClients);
                break;

            case "send-rate":
                if (words.length != 2) {
                    System.err.println("Usage: send-rate <rate>");
                    break;
                }
                int rate = Integer.parseInt(words[1]);
                setSendRate(rate);
                break;

            case "file":
                if (words.length != 2) {
                    System.err.println("Usage: file <path>");
                    break;
                }
                String path = words[1];
                setFile(path);
                break;

            case "clear":
                clearOutput();
                break;

            default:
                System.err.println("Unknown command: " + words[0]);
                break;
        }

        clearInput();
    }

    public void clearOutput() {
        this.outputWindow.clearOutput();
    }

    public void clearInput() {
        this.controllWindow.ClearInput();
    }

    public void stopRun() {
        // Stop the current runs
        System.err.println("Ctrl + S");
    }

    public void runCommand(String command) throws IOException {
        // Run the command
        System.err.println("Run: " + command);

        String[] words = command.split(" ");
        String path;
        switch (words.length) {
            case 2:
                path = words[1];
                // Run file
                break;
            case 4:
                // Run file with clients and rate
                path = words[1];
                int numClients = Integer.parseInt(words[2]);
                int sendRate = Integer.parseInt(words[3]);
                
                File filebuffer = new File(path);
                byte[] byteArray = new byte[(int) filebuffer.length()];
                try (FileInputStream is = new FileInputStream(filebuffer)) {
                    is.read(byteArray);
                }

                for (int i = 0; i < numClients; i++) {
                    WorkerHandler.addClient(path + i, byteArray, sendRate);
                }

                break;
            default:
                System.err.println("Usage: run <file> [<clients> <rate>]");
                break;
        }
    }

    public void setClients(int numClients) {
        // Set the number of clients
        System.err.println("Clients: " + numClients);
    }

    public void setSendRate(int rate) {
        // Set the send rate
        System.err.println("Send rate: " + rate);
    }

    public void setFile(String path) {
        // Set the file path
        System.err.println("File: " + path);
    }
}
