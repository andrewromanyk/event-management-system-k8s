package ua.edu.ukma.event_management_micro;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("api/static")
public class StaticControllerApi {

    @GetMapping(value = "icon", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> icon() throws IOException {
        Path path = Paths.get("src/main/resources/static/em-icon.png");
        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"icon.png\"")
                .contentType(MediaType.IMAGE_PNG).body(resource);
    }
}