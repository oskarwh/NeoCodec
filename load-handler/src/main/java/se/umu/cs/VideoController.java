package se.umu.cs;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/converter")
public class VideoController {

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public NeoFile submit(@RequestBody NeoFile file) {
        System.out.printf("New file submitted. From %d to %d.", file.getSourceType(), file.getTargetType());
        return file;
    }
}
