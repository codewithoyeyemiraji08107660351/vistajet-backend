package com.vistajet.vistajet.temp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;


@RestController
public class TempController {

    @GetMapping("/debug/files")
    public List<String> listFiles() throws IOException {
        Path path = Paths.get("/railway/volume");
        try (Stream<Path> walk = Files.walk(path)) {
            return walk
                    .map(Path::toString)
                    .toList();
        }
    }

}
