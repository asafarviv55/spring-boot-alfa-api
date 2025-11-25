package com.example.alfa.controllers;

import com.example.alfa.models.PerformanceGoal;
import com.example.alfa.models.PerformanceReview;
import com.example.alfa.services.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/performance")
public class PerformanceController {

    @Autowired
    private PerformanceService performanceService;

    // Reviews
    @PostMapping("/review")
    public ResponseEntity<PerformanceReview> createReview(
            @RequestParam Long employeeId,
            @RequestParam Long reviewerId,
            @RequestParam String period,
            @RequestParam PerformanceReview.ReviewType type,
            @RequestParam String dueDate) {
        return ResponseEntity.ok(performanceService.createReview(
                employeeId, reviewerId, period, type, LocalDate.parse(dueDate)));
    }

    @PostMapping("/review/{id}/submit")
    public ResponseEntity<PerformanceReview> submitRatings(
            @PathVariable Long id,
            @RequestBody Map<String, Object> ratings) {
        return ResponseEntity.ok(performanceService.submitRatings(
                id,
                (Integer) ratings.get("performance"),
                (Integer) ratings.get("communication"),
                (Integer) ratings.get("teamwork"),
                (Integer) ratings.get("leadership"),
                (Integer) ratings.get("innovation"),
                (Integer) ratings.get("attendance"),
                (String) ratings.get("strengths"),
                (String) ratings.get("improvements"),
                (String) ratings.get("achievements"),
                (String) ratings.get("goals"),
                (String) ratings.get("comments")
        ));
    }

    @PostMapping("/review/{id}/acknowledge")
    public ResponseEntity<PerformanceReview> acknowledgeReview(
            @PathVariable Long id,
            @RequestParam Long employeeId,
            @RequestParam(required = false) String comments) {
        return ResponseEntity.ok(performanceService.acknowledgeReview(id, employeeId, comments));
    }

    @GetMapping("/review/employee/{employeeId}")
    public ResponseEntity<List<PerformanceReview>> getEmployeeReviews(@PathVariable Long employeeId) {
        return ResponseEntity.ok(performanceService.getEmployeeReviews(employeeId));
    }

    @GetMapping("/review/pending/reviewer/{reviewerId}")
    public ResponseEntity<List<PerformanceReview>> getPendingReviewsForReviewer(@PathVariable Long reviewerId) {
        return ResponseEntity.ok(performanceService.getPendingReviewsForReviewer(reviewerId));
    }

    @GetMapping("/review/pending/acknowledge/{employeeId}")
    public ResponseEntity<List<PerformanceReview>> getPendingAcknowledgements(@PathVariable Long employeeId) {
        return ResponseEntity.ok(performanceService.getPendingAcknowledgements(employeeId));
    }

    // Goals
    @PostMapping("/goal")
    public ResponseEntity<PerformanceGoal> createGoal(
            @RequestParam Long employeeId,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam PerformanceGoal.GoalCategory category,
            @RequestParam String targetDate,
            @RequestParam(defaultValue = "1") Integer weight) {
        return ResponseEntity.ok(performanceService.createGoal(
                employeeId, title, description, category, LocalDate.parse(targetDate), weight));
    }

    @PutMapping("/goal/{id}/progress")
    public ResponseEntity<PerformanceGoal> updateGoalProgress(
            @PathVariable Long id,
            @RequestParam Integer progress,
            @RequestParam(required = false) String notes) {
        return ResponseEntity.ok(performanceService.updateGoalProgress(id, progress, notes));
    }

    @GetMapping("/goal/employee/{employeeId}")
    public ResponseEntity<List<PerformanceGoal>> getEmployeeGoals(@PathVariable Long employeeId) {
        return ResponseEntity.ok(performanceService.getEmployeeGoals(employeeId));
    }

    @GetMapping("/goal/active/{employeeId}")
    public ResponseEntity<List<PerformanceGoal>> getActiveGoals(@PathVariable Long employeeId) {
        return ResponseEntity.ok(performanceService.getActiveGoals(employeeId));
    }
}
