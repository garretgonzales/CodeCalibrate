package com.codecalibrate.domain;

import com.codecalibrate.data.ExerciseRepository;
import com.codecalibrate.domain.content.ExerciseContentDefinition;
import com.codecalibrate.domain.content.GitHubExerciseContentClient;
import com.codecalibrate.dto.ExerciseReferenceResponse;
import com.codecalibrate.dto.ExerciseResponse;
import com.codecalibrate.dto.SkillResponse;
import com.codecalibrate.models.Exercise;
import java.net.URI;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ExerciseService {

  private static final Set<String> ALLOWED_REFERENCE_HOSTS = Set.of("docs.oracle.com", "dev.java");
  private final ExerciseRepository exerciseRepository;
  private final GitHubExerciseContentClient gitHubExerciseContentClient;

  public ExerciseService(
      ExerciseRepository exerciseRepository,
      GitHubExerciseContentClient gitHubExerciseContentClient) {
    this.exerciseRepository = exerciseRepository;
    this.gitHubExerciseContentClient = gitHubExerciseContentClient;
  }

  public ExerciseResponse getExerciseById(Integer id) {
    Exercise exercise =
        exerciseRepository.findById(id).orElseThrow(() -> new ExerciseNotFoundException(id));

    ExerciseContentDefinition contentDefinition =
        gitHubExerciseContentClient.getExerciseContent(exercise.getExternalId());

    if (!exercise.getExternalId().equals(contentDefinition.id())) {
      throw new IllegalStateException("Exercise content does not match the requested exercise.");
    }

    return toResponse(exercise, contentDefinition);
  }

  private ExerciseResponse toResponse(
      Exercise exercise, ExerciseContentDefinition contentDefinition) {
    List<SkillResponse> skills =
        exercise.getSkills().stream()
            .map(
                skill ->
                    new SkillResponse(
                        skill.getId(),
                        skill.getName(),
                        skill.getDescription(),
                        skill.getDifficulty()))
            .toList();

    List<ExerciseReferenceResponse> references =
        contentDefinition.references() == null
            ? List.of()
            : contentDefinition.references().stream().map(this::toReferenceResponse).toList();

    return new ExerciseResponse(
        exercise.getId(),
        exercise.getExternalId(),
        exercise.getTitle(),
        exercise.getDescription(),
        exercise.getDifficulty(),
        exercise.getSource(),
        contentDefinition.starterCode(),
        references,
        skills);
  }

  private ExerciseReferenceResponse toReferenceResponse(
      ExerciseContentDefinition.DocumentationReference reference) {
    if (reference == null
        || reference.label() == null
        || reference.label().isBlank()
        || reference.description() == null
        || reference.description().isBlank()
        || reference.url() == null
        || reference.url().isBlank()) {
      throw new IllegalStateException("Exercise documentation reference is invalid.");
    }

    URI uri;

    try {
      uri = URI.create(reference.url());
    } catch (IllegalArgumentException exception) {
      throw new IllegalStateException("Exercise documentation reference is invalid.", exception);
    }

    String host = uri.getHost();

    if (!"https".equalsIgnoreCase(uri.getScheme())
        || host == null
        || ALLOWED_REFERENCE_HOSTS.stream()
            .noneMatch(allowedHost -> allowedHost.equalsIgnoreCase(host))) {
      throw new IllegalStateException("Exercise documentation reference is not approved.");
    }

    return new ExerciseReferenceResponse(
        reference.label(), reference.description(), uri.toString());
  }
}
