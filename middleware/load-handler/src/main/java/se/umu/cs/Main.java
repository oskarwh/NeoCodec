package se.umu.cs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import se.umu.cs.pulsar.PulsarController;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
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