package com.devserbyn.suncheck.utility;

import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResourceUtil {

    private final ResourceLoader resourceLoader;

    public File loadResourceFile(String filePath) {
        File searchedFile = null;
        try {
            searchedFile = resourceLoader.getResource("classpath:" + filePath).getFile();
        } catch (IOException e) {
            log.warn(String.format("Resource file %s wasn't loaded", filePath), e);
        }
        return searchedFile;
    }

    public String readResourceFileLines(String filePath) {
        StringBuilder fileContentBuilder = new StringBuilder();
        try {
            Files.readAllLines(this.loadResourceFile(filePath).toPath())
                    .forEach(l -> fileContentBuilder.append(l).append("\n"));
        } catch (IOException e) {
            log.warn(String.format("Error reading lines from resource file %s", filePath), e);
        }
        return fileContentBuilder.toString();
    }
}
