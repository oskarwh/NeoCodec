package se.umu.cs;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import se.umu.cs.pulsar.PClient;
import se.umu.cs.pulsar.PulsarController;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/converter")
public class VideoController {

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<NeoFile> submit(@RequestBody NeoFile file, @RequestParam boolean trash) {
        System.out.printf("New file submitted. From %d to %d.", file.getSourceType(), file.getTargetType());

        String clientId = PulsarController.getNextId();

        String id = PulsarController.createClientTopic(clientId);

        PClient client = new PClient(id, PulsarController.getBrokers());

        NeoFile result;
        try {
            client.send(file);

            result = client.receive();
        } catch (IOException e) {
            System.err.println("Failed to produce over pulsar: " + e.getMessage());
            PulsarController.removeClientTopic(id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        PulsarController.removeClientTopic(id);

        if (trash) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
