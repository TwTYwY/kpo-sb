package com.example.filestoring.controller;

import com.example.filestoring.model.Work;
import com.example.filestoring.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/works")
public class WorkController {

    @Autowired
    private WorkService workService;

    @PostMapping
    public ResponseEntity<Work> submitWork(
            @RequestParam String studentName,
            @RequestParam String taskId,
            @RequestParam MultipartFile file) {
        Work savedWork = workService.saveWork(studentName, taskId, file);
        return ResponseEntity.ok(savedWork);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Work> getWork(@PathVariable Long id) {
        return workService.getWorkById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("File Storing Service is UP");
    }
}