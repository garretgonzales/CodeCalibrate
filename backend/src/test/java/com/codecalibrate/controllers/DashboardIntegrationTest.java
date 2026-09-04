package com.codecalibrate.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codecalibrate.data.UserRepository;
import com.codecalibrate.domain.DashboardService;
import com.codecalibrate.domain.JwtService;
import com.codecalibrate.dto.DashboardExerciseSummaryResponse;
import com.codecalibrate.dto.DashboardOverviewResponse;
import com.codecalibrate.dto.DashboardResponse;
import com.codecalibrate.dto.DashboardUserResponse;
import com.codecalibrate.models.User;
import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class DashboardIntegrationTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private UserRepository userRepository;

    @Autowired private JwtService jwtService;

    @MockitoBean private DashboardService dashboardService;

    private User user;
    private String token;

    @BeforeEach
    void setUp() {
        String testId = UUID.randomUUID().toString().replace("-", "");

        user =
                userRepository.save(
                        new User(
                                "dashboard-" + testId,
                                "dashboard-" + testId + "@example.com",
                                "not-a-real-password-hash"));

        token = jwtService.generateToken(user);

        DashboardResponse response =
                new DashboardResponse(
                        new DashboardUserResponse(
                                user.getUsername(),
                                user.getEmail(),
                                user.getCreatedAt().toInstant(ZoneOffset.UTC)),
                        new DashboardOverviewResponse(
                                0,
                                0,
                                0,
                                new BigDecimal("0.00"),
                                new BigDecimal("0.00")),
                        new DashboardExerciseSummaryResponse(
                                1,
                                "Print an Age Variable",
                                "Declare and print an integer variable.",
                                "Beginner",
                                List.of("Variables")),
                        List.of(),
                        List.of(),
                        List.of());

        when(dashboardService.getDashboard(any(User.class)))
                .thenReturn(response);
    }

    @Test
    void shouldReturnDashboardForAuthenticatedUser() throws Exception {
        mockMvc
                .perform(
                        get("/api/dashboard")
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.user.username")
                                .value(user.getUsername()))
                .andExpect(
                        jsonPath("$.user.email")
                                .value(user.getEmail()))
                .andExpect(
                        jsonPath("$.overview.totalAttempts")
                                .value(0))
                .andExpect(
                        jsonPath("$.recommendedExercise.id")
                                .value(1))
                .andExpect(jsonPath("$.skillMastery").isArray())
                .andExpect(jsonPath("$.recentAttempts").isArray())
                .andExpect(jsonPath("$.pathProgress").isArray())
                .andExpect(
                        jsonPath("$.user.passwordHash")
                                .doesNotExist());

        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(dashboardService).getDashboard(userCaptor.capture());

        assertThat(userCaptor.getValue().getId())
                .isEqualTo(user.getId());
    }

    @Test
    void shouldRejectDashboardRequestWithoutToken()
            throws Exception {
        mockMvc
                .perform(get("/api/dashboard"))
                .andExpect(status().isUnauthorized());
    }
}
