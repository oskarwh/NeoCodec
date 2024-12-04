package se.umu.cs.controller;

import se.umu.cs.windows.ControllWindow;
import se.umu.cs.windows.OutputWindow;

public class CommandController {
    private ControllWindow controllWindow = null;
    private OutputWindow outputWindow = null;
    
    public CommandController() { }

    public CommandController(ControllWindow cw, OutputWindow ow) {
        this.controllWindow = cw;
        this.outputWindow = ow;
    }

    public void handleCommand(String command) {
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
        // Stop the current run
        System.err.println("Ctrl + S");
    }

    public void runCommand(String command) {
        // Run the command
        System.err.println("Run: " + command);

        String[] words = command.split(" ");
        if (words.length == 2) {
            // Run file

        } else if (words.length == 4) {
            // Run file with clients and rate

        } else {
            System.err.println("Usage: run <file> [<clients> <rate>]");
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
