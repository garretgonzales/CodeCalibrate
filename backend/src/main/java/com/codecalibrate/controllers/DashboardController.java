package com.codecalibrate.controllers;

import com.codecalibrate.domain.DashboardService;
import com.codecalibrate.dto.DashboardResponse;
import com.codecalibrate.models.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public DashboardResponse getDashboard(
            @AuthenticationPrincipal User user) {
        return dashboardService.getDashboard(user);
    }
}
