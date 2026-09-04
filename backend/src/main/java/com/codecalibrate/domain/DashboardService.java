package com.codecalibrate.domain;

import com.codecalibrate.data.AttemptRepository;
import com.codecalibrate.data.ExerciseRepository;
import com.codecalibrate.data.LearningPathRepository;
import com.codecalibrate.data.UserMasteryRepository;
import com.codecalibrate.dto.DashboardAttemptResponse;
import com.codecalibrate.dto.DashboardExerciseSummaryResponse;
import com.codecalibrate.dto.DashboardOverviewResponse;
import com.codecalibrate.dto.DashboardPathProgressResponse;
import com.codecalibrate.dto.DashboardPathSkillResponse;
import com.codecalibrate.dto.DashboardResponse;
import com.codecalibrate.dto.DashboardSkillMasteryResponse;
import com.codecalibrate.dto.DashboardUserResponse;
import com.codecalibrate.models.Attempt;
import com.codecalibrate.models.Exercise;
import com.codecalibrate.models.LearningPath;
import com.codecalibrate.models.Skill;
import com.codecalibrate.models.User;
import com.codecalibrate.models.UserMastery;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private static final BigDecimal ZERO_PERCENT = BigDecimal.ZERO.setScale(2);

    private final AttemptRepository attemptRepository;
    private final ExerciseRepository exerciseRepository;
    private final LearningPathRepository learningPathRepository;
    private final UserMasteryRepository userMasteryRepository;
    private final RecommendationService recommendationService;

    public DashboardService(
            AttemptRepository attemptRepository,
            ExerciseRepository exerciseRepository,
            LearningPathRepository learningPathRepository,
            UserMasteryRepository userMasteryRepository,
            RecommendationService recommendationService) {
        this.attemptRepository = attemptRepository;
        this.exerciseRepository = exerciseRepository;
        this.learningPathRepository = learningPathRepository;
        this.userMasteryRepository = userMasteryRepository;
        this.recommendationService = recommendationService;
    }

    public DashboardResponse getDashboard(User user) {
        List<UserMastery> masteries =
                userMasteryRepository.findByUserOrderByMasteryScoreAscLastPracticedAtAsc(user);

        Set<Integer> completedExerciseIds =
                new HashSet<>(attemptRepository.findDistinctCorrectExerciseIdsByUser(user));

        Map<Integer, UserMastery> masteryBySkillId =
                masteries.stream()
                         .collect(
                                 Collectors.toMap(
                                         mastery -> mastery.getSkill().getId(),
                                         Function.identity()));

        Map<Integer, List<Exercise>> exercisesBySkillId = new HashMap<>();

        List<DashboardSkillMasteryResponse> skillMastery =
                masteries.stream()
                         .map(this::mapSkillMastery)
                         .toList();

        List<DashboardAttemptResponse> recentAttempts =
                attemptRepository.findTop5ByUserOrderByAttemptedAtDesc(user).stream()
                                 .map(this::mapAttempt)
                                 .toList();

        List<DashboardPathProgressResponse> pathProgress =
                learningPathRepository.findAll().stream()
                                      .sorted(Comparator.comparing(LearningPath::getId))
                                      .map(
                                              path ->
                                                      mapPathProgress(
                                                              path,
                                                              masteryBySkillId,
                                                              completedExerciseIds,
                                                              exercisesBySkillId))
                                      .toList();

        Exercise recommendedExercise =
                recommendationService.recommendNextExercise(user);

        return new DashboardResponse(
                mapUser(user),
                buildOverview(user, masteries, completedExerciseIds),
                mapExercise(recommendedExercise),
                skillMastery,
                recentAttempts,
                pathProgress);
    }

    private DashboardUserResponse mapUser(User user) {
        return new DashboardUserResponse(
                user.getUsername(),
                user.getEmail(),
                toInstant(user.getCreatedAt()));
    }

    private DashboardOverviewResponse buildOverview(
            User user,
            List<UserMastery> masteries,
            Set<Integer> completedExerciseIds) {
        long totalAttempts = attemptRepository.countByUser(user);
        long correctAttempts = attemptRepository.countByUserAndCorrectTrue(user);

        return new DashboardOverviewResponse(
                totalAttempts,
                correctAttempts,
                completedExerciseIds.size(),
                calculatePercentage(correctAttempts, totalAttempts),
                calculateAverageMastery(masteries));
    }

    private DashboardSkillMasteryResponse mapSkillMastery(
            UserMastery mastery) {
        Skill skill = mastery.getSkill();

        return new DashboardSkillMasteryResponse(
                skill.getId(),
                skill.getName(),
                skill.getDescription(),
                skill.getDifficulty(),
                mastery.getMasteryScore(),
                mastery.getQuestionsAttempted(),
                mastery.getQuestionsCorrect(),
                toInstant(mastery.getLastPracticedAt()));
    }

    private DashboardAttemptResponse mapAttempt(Attempt attempt) {
        return new DashboardAttemptResponse(
                attempt.getExercise().getId(),
                attempt.getExercise().getTitle(),
                attempt.isCorrect(),
                toInstant(attempt.getAttemptedAt()));
    }

    private DashboardExerciseSummaryResponse mapExercise(
            Exercise exercise) {
        return new DashboardExerciseSummaryResponse(
                exercise.getId(),
                exercise.getTitle(),
                exercise.getDescription(),
                exercise.getDifficulty(),
                exercise.getSkills().stream()
                        .map(Skill::getName)
                        .toList());
    }

    private DashboardPathProgressResponse mapPathProgress(
            LearningPath path,
            Map<Integer, UserMastery> masteryBySkillId,
            Set<Integer> completedExerciseIds,
            Map<Integer, List<Exercise>> exercisesBySkillId) {
        List<DashboardPathSkillResponse> skills =
                path.getSkills().stream()
                    .map(
                            skill ->
                                    mapPathSkill(
                                            skill,
                                            masteryBySkillId,
                                            completedExerciseIds,
                                            exercisesBySkillId))
                    .toList();

        Set<Integer> pathExerciseIds =
                path.getSkills().stream()
                    .flatMap(
                            skill ->
                                    getExercisesForSkill(skill, exercisesBySkillId)
                                            .stream())
                    .map(Exercise::getId)
                    .collect(Collectors.toSet());

        int completedExercises =
                (int)
                        pathExerciseIds.stream()
                                       .filter(completedExerciseIds::contains)
                                       .count();

        return new DashboardPathProgressResponse(
                path.getId(),
                path.getName(),
                path.getLanguage(),
                completedExercises,
                pathExerciseIds.size(),
                skills);
    }

    private DashboardPathSkillResponse mapPathSkill(
            Skill skill,
            Map<Integer, UserMastery> masteryBySkillId,
            Set<Integer> completedExerciseIds,
            Map<Integer, List<Exercise>> exercisesBySkillId) {
        UserMastery mastery = masteryBySkillId.get(skill.getId());
        List<Exercise> exercises =
                getExercisesForSkill(skill, exercisesBySkillId);

        int completedExercises =
                (int)
                        exercises.stream()
                                 .map(Exercise::getId)
                                 .filter(completedExerciseIds::contains)
                                 .count();

        return new DashboardPathSkillResponse(
                skill.getId(),
                skill.getName(),
                mastery != null,
                mastery == null ? ZERO_PERCENT : mastery.getMasteryScore(),
                completedExercises,
                exercises.size());
    }

    private List<Exercise> getExercisesForSkill(
            Skill skill,
            Map<Integer, List<Exercise>> exercisesBySkillId) {
        return exercisesBySkillId.computeIfAbsent(
                skill.getId(),
                ignored ->
                        exerciseRepository.findBySkillsContainingOrderByIdAsc(skill));
    }

    private BigDecimal calculatePercentage(
            long numerator,
            long denominator) {
        if (denominator == 0) {
            return ZERO_PERCENT;
        }

        return BigDecimal.valueOf(numerator)
                         .multiply(BigDecimal.valueOf(100))
                         .divide(
                                 BigDecimal.valueOf(denominator),
                                 2,
                                 RoundingMode.HALF_UP);
    }

    private BigDecimal calculateAverageMastery(
            List<UserMastery> masteries) {
        if (masteries.isEmpty()) {
            return ZERO_PERCENT;
        }

        BigDecimal total =
                masteries.stream()
                         .map(UserMastery::getMasteryScore)
                         .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.divide(
                BigDecimal.valueOf(masteries.size()),
                2,
                RoundingMode.HALF_UP);
    }

    private Instant toInstant(LocalDateTime value) {
        return value == null
                ? null
                : value.toInstant(ZoneOffset.UTC);
    }
}
