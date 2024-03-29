package com.crcl.common.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileHelper {
    private final ResourceLoader resourceLoader;

    public String loadFileAsString(String filePath) {
        try {
            Resource resource = resourceLoader.getResource("classpath:" + filePath);
            try (InputStream inputStream = resource.getInputStream()) {
                byte[] fileBytes = FileCopyUtils.copyToByteArray(inputStream);
                return new String(fileBytes, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.error("Error when loading file: {}", filePath, e);
            throw new RuntimeException(e);
        }
    }

    public byte[] loadFileAsBytes(String filePath) {
        try {
            Resource resource = resourceLoader.getResource("classpath:" + filePath);
            try (InputStream inputStream = resource.getInputStream()) {
                return FileCopyUtils.copyToByteArray(inputStream);
            }
        } catch (IOException e) {
            log.error("Error when loading file: {}", filePath, e);
            throw new RuntimeException(e);
        }
    }

    public String loadBufferedFileAsString(String filePath) {
        try {
            Resource resource = resourceLoader.getResource("classpath:" + filePath);
            try (InputStream inputStream = resource.getInputStream()) {
                byte[] fileBytes = FileCopyUtils.copyToByteArray(inputStream);
                return new String(fileBytes, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.error("Error when loading buffered file: {}", filePath, e);
            throw new RuntimeException(e);
        }
    }

    public byte[] loadBufferedFileAsBytes(String filePath) {
        try {
            Resource resource = resourceLoader.getResource("classpath:" + filePath);
            try (InputStream inputStream = resource.getInputStream()) {
                return FileCopyUtils.copyToByteArray(inputStream);
            }
        } catch (IOException e) {
            log.error("Error when loading buffered file: {}", filePath, e);
            throw new RuntimeException(e);
        }
    }
}
