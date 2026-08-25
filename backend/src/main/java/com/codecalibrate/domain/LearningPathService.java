package com.codecalibrate.domain;


import com.codecalibrate.data.LearningPathRepository;
import com.codecalibrate.dto.LearningPathResponse;
import com.codecalibrate.dto.SkillResponse;
import com.codecalibrate.models.LearningPath;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class LearningPathService {

    private final LearningPathRepository learningPathRepository;

    public LearningPathService(LearningPathRepository learningPathRepository) {
        this.learningPathRepository = learningPathRepository;
    }

    public List<LearningPathResponse> getAllLearningPaths() {
        return learningPathRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public LearningPathResponse getLearningPathById(Integer id) {
        LearningPath learningPath = learningPathRepository.findById(id)
                .orElseThrow(() -> new LearningPathNotFoundException(id));

        return toResponse(learningPath);
    }

    private LearningPathResponse toResponse(LearningPath learningPath) {
        List<SkillResponse> skills = learningPath.getSkills().stream()
                .map(skill -> new SkillResponse(
                        skill.getId(),
                        skill.getName(),
                        skill.getDescription(),
                        skill.getDifficulty()
                ))
                .toList();

        return new LearningPathResponse(
                learningPath.getId(),
                learningPath.getName(),
                learningPath.getDescription(),
                learningPath.getLanguage(),
                skills
        );
    }

}
