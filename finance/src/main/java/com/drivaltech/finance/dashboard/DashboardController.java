package com.drivaltech.finance.dashboard;

import com.drivaltech.finance.dto.DashboardSummaryResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

   /* @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/dashboard/summary")
    public ResponseEntity<DashboardSummaryResponse> getSummary(

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        return ResponseEntity.ok(
                dashboardService.getSummary(startDate, endDate)
        );
    }*/

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
            LocalDate endDate
    ) {
        return ResponseEntity.ok(
                dashboardService.getSummary(startDate, endDate)
        );
    }
}
