package com.example.filestoring.service;

import com.example.filestoring.model.Work;
import com.example.filestoring.repository.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class WorkService {

    private final String UPLOAD_DIR = "./uploads/";

    @Autowired
    private WorkRepository workRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Work saveWork(String studentName, String taskId, MultipartFile file) {
        Work work = new Work();

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            work.setStudentName(studentName);
            work.setTaskId(taskId);
            work.setFileName(file.getOriginalFilename());
            work.setFilePath(filePath.toString());
            work.setSubmittedAt(LocalDateTime.now());

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении файла", e);
        }

        Work savedWork = workRepository.save(work);

        new Thread(() -> {
            try {
                String analysisUrl = "http://file-analysis-service:8082/reports/analyze";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                Map<String, Object> request = new HashMap<>();
                request.put("workId", savedWork.getId());
                request.put("taskId", taskId);

                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
                restTemplate.postForObject(analysisUrl, entity, String.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        return savedWork;
    }

    public Optional<Work> getWorkById(Long id) {
        return workRepository.findById(id);
    }
}