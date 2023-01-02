package com.crcl.storage.controller;

import com.crcl.storage.service.StorageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLConnection;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("files")
@AllArgsConstructor
@Slf4j
public class StorageController {
    private final StorageService storageService;

    @GetMapping("/{objectName}/{tag}")
    public ResponseEntity<?> getObject(@PathVariable("objectName") String objectName, @PathVariable("tag") String tag) {
        final var resource = storageService.getResource(objectName, tag);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(URLConnection.guessContentTypeFromName(objectName)))
                .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS).cachePrivate().proxyRevalidate())
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.contentLength()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"%s\"".formatted(objectName))
                .body(resource);
    }

    @PostMapping()
    public ResponseEntity<?> save(@RequestParam("file") MultipartFile multipartFile) {
        final var response = storageService.save(multipartFile);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/all")
    public ResponseEntity<?> saveAll(@RequestParam("files") MultipartFile[] multipartFile) {
        final var responses = storageService.saveAll(multipartFile);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}