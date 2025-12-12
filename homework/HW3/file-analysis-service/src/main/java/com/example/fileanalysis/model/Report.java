package com.example.fileanalysis.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long workId;
    private String status;
    private Boolean plagiarismFlag;
    private String reportPath;
    private LocalDateTime createdAt;

    public Report() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getWorkId() { return workId; }
    public void setWorkId(Long workId) { this.workId = workId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Boolean getPlagiarismFlag() { return plagiarismFlag; }
    public void setPlagiarismFlag(Boolean plagiarismFlag) { this.plagiarismFlag = plagiarismFlag; }

    public String getReportPath() { return reportPath; }
    public void setReportPath(String reportPath) { this.reportPath = reportPath; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}