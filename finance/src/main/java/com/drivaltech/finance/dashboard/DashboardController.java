package com.drivaltech.finance.dashboard;

import com.drivaltech.finance.dto.DashboardAnalyticsResponse;
import com.drivaltech.finance.dto.DashboardSummaryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
public class DashboardController {

    private final DashboardService dashboardService;

    private final DashboardAnalyticsService dashboardAnalyticsService;

    public DashboardController(DashboardService dashboardService,
                               DashboardAnalyticsService dashboardAnalyticsService) {
        this.dashboardService = dashboardService;
        this.dashboardAnalyticsService = dashboardAnalyticsService;
    }

    @GetMapping("/dashboard/summary")
    public ResponseEntity<DashboardSummaryResponse> getSummary(

            @RequestParam(required = false)
            @org.springframework.format.annotation.DateTimeFormat(
                    iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE
            )
            LocalDate startDate,

            @RequestParam(required = false)
            @org.springframework.format.annotation.DateTimeFormat(
                    iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE
            )
            LocalDate endDate,

            @RequestParam(required = false)
            UUID categoryId,

            /*@RequestParam(required = false)
            TransactionType type*/
            @RequestParam(required = false) String type
    ) {
        return ResponseEntity.ok(
                dashboardService.getSummary(startDate, endDate, categoryId, type)
        );
    }

    @GetMapping("/dashboard/analytics")
    public ResponseEntity<DashboardAnalyticsResponse> getAnalytics(

            @RequestParam
            @org.springframework.format.annotation.DateTimeFormat(
                    iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE
            )
            LocalDate startDate,

            @RequestParam
            @org.springframework.format.annotation.DateTimeFormat(
                    iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE
            )
            LocalDate endDate
    ) {
        return ResponseEntity.ok(
                dashboardAnalyticsService.getAnalytics(startDate, endDate)
        );
    }
}