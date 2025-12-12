package com.smartuniversity.exam.controller;

import com.smartuniversity.exam.entity.Exam;
import com.smartuniversity.exam.service.ExamService;
import com.smartuniversity.exam.dto.CreateExamRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {
    
    private final ExamService examService;
    
    public ExamController(ExamService examService) {
        this.examService = examService;
    }
    
    /**
     * POST /api/exams
     * Creates a new exam and publishes event to RabbitMQ
     */
    @PostMapping
    public ResponseEntity<Exam> createExam(@RequestBody CreateExamRequest request) {
        Exam exam = examService.createExam(request.getTitle(), request.getDate());
        return ResponseEntity.ok(exam);
    }
    
    /**
     * GET /api/exams
     * Returns all exams
     */
    @GetMapping
    public ResponseEntity<List<Exam>> getAllExams() {
        return ResponseEntity.ok(examService.getAllExams());
    }
    
    /**
     * GET /api/exams/{id}
     * Returns a specific exam by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Exam> getExamById(@PathVariable String id) {
        return examService.getExamById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
