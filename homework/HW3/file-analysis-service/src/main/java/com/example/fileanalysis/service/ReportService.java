package com.example.fileanalysis.service;

import com.example.fileanalysis.model.Report;
import com.example.fileanalysis.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public void analyzeWork(Long workId, String taskId) {
        List<Report> existing = reportRepository.findAll();
        boolean plagiarism = existing.stream()
                .anyMatch(r -> r.getWorkId() != null && !r.getWorkId().equals(workId));

        Report report = new Report();
        report.setWorkId(workId);
        report.setStatus("COMPLETED");
        report.setPlagiarismFlag(plagiarism);
        report.setReportPath("/reports/report_" + workId + ".json");
        report.setCreatedAt(LocalDateTime.now());

        reportRepository.save(report);
    }

    public List<Report> getReportsByWorkId(Long workId) {
        return reportRepository.findByWorkId(workId);
    }
}