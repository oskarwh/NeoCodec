package se.umu.cs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        //if (args.length != 2)
        //    throw new IllegalArgumentException("Usage:\n\tjava -jar /path/to/file.jar <port> <brokers>\n");
//
        //int port = Integer.parseInt(args[0]);
        //String brokers = args[1];

        PulsarController.init();
        
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner CommandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("Admin client is live.");
        };
    }
}