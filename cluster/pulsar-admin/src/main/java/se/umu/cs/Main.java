package se.umu.cs;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1)
            throw new IllegalArgumentException("Usage:\n\tjava -jar /path/to/file.jar <port> <brokers>\n");

        int port = Integer.valueOf(args[0]);
        String brokers = args[1];

        AdminServer server = new AdminServer(port, brokers);
    }
}