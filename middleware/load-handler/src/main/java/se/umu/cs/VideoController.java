package se.umu.cs;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Integer> getId() {
        String id = PulsarController.getNextId();
        PulsarController.createClientTopic(id);
        return ResponseEntity.status(HttpStatus.OK).body(Integer.valueOf(id));
    }
}
