package com.codecalibrate.controllers;


import com.codecalibrate.domain.LearningPathService;
import com.codecalibrate.dto.LearningPathResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/learning-paths")
public class LearningPathController {

    private final LearningPathService learningPathService;

    public LearningPathController(LearningPathService learningPathService) {
        this.learningPathService = learningPathService;
    }

    @GetMapping
    public List<LearningPathResponse> getAllLearningPaths() {
        return learningPathService.getAllLearningPaths();
    }
    @GetMapping("/{id}")
    public LearningPathResponse getLearningPathById(@PathVariable Integer id) {
        return learningPathService.getLearningPathById(id);
    }

}
