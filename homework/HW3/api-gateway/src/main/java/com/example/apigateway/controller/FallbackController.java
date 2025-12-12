package com.example.apigateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/file-storing")
    public ResponseEntity<String> fileStoringFallback() {
        return ResponseEntity.status(503)
                .body("File Storing Service is unavailable. Please try again later.");
    }

    @GetMapping("/file-analysis")
    public ResponseEntity<String> fileAnalysisFallback() {
        return ResponseEntity.status(503)
                .body("File Analysis Service is unavailable. Please try again later.");
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("API Gateway is UP");
    }
}