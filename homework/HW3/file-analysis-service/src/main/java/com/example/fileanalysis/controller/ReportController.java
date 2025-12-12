package com.example.fileanalysis.controller;

import com.example.fileanalysis.model.Report;
import com.example.fileanalysis.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeWork(@RequestBody Map<String, Object> request) {
        Long workId = Long.valueOf(request.get("workId").toString());
        String taskId = (String) request.get("taskId");

        reportService.analyzeWork(workId, taskId);
        return ResponseEntity.ok("Анализ запущен для работы ID: " + workId);
    }

    @GetMapping("/work/{workId}")
    public ResponseEntity<List<Report>> getReportsByWorkId(@PathVariable Long workId) {
        return ResponseEntity.ok(reportService.getReportsByWorkId(workId));
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("File Analysis Service is UP");
    }
}