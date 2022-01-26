package com.example.elasticsearchdemo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
public class ResourceUtil {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public String loadAsString(final String path) {
        try {
            final File resource = new ClassPathResource(path).getFile();

            return new String(Files.readAllBytes(resource.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
