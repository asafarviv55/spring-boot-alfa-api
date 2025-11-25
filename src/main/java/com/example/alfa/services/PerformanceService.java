package com.example.alfa.services;

import com.example.alfa.models.*;
import com.example.alfa.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PerformanceService {

    @Autowired
    private PerformanceReviewRepository reviewRepository;

    @Autowired
    private PerformanceGoalRepository goalRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public PerformanceReview createReview(Long employeeId, Long reviewerId, String period,
                                          PerformanceReview.ReviewType type, LocalDate dueDate) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Employee reviewer = employeeRepository.findById(reviewerId)
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));

        PerformanceReview review = PerformanceReview.builder()
                .employee(employee)
                .reviewer(reviewer)
                .reviewPeriod(period)
                .reviewType(type)
                .dueDate(dueDate)
                .status(PerformanceReview.ReviewStatus.DRAFT)
                .build();

        return reviewRepository.save(review);
    }

    @Transactional
    public PerformanceReview submitRatings(Long reviewId, Integer performance, Integer communication,
                                           Integer teamwork, Integer leadership, Integer innovation,
                                           Integer attendance, String strengths, String improvements,
                                           String achievements, String goals, String comments) {
        PerformanceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setPerformanceRating(performance);
        review.setCommunicationRating(communication);
        review.setTeamworkRating(teamwork);
        review.setLeadershipRating(leadership);
        review.setInnovationRating(innovation);
        review.setAttendanceRating(attendance);
        review.setStrengths(strengths);
        review.setAreasForImprovement(improvements);
        review.setAchievements(achievements);
        review.setGoals(goals);
        review.setReviewerComments(comments);
        review.setReviewDate(LocalDate.now());
        review.setStatus(PerformanceReview.ReviewStatus.PENDING_ACKNOWLEDGEMENT);
        review.calculateOverallRating();

        return reviewRepository.save(review);
    }

    @Transactional
    public PerformanceReview acknowledgeReview(Long reviewId, Long employeeId, String comments) {
        PerformanceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getEmployee().getId().equals(employeeId)) {
            throw new RuntimeException("Only the reviewed employee can acknowledge");
        }

        review.setEmployeeAcknowledged(true);
        review.setAcknowledgedAt(LocalDateTime.now());
        review.setEmployeeComments(comments);
        review.setStatus(PerformanceReview.ReviewStatus.COMPLETED);

        return reviewRepository.save(review);
    }

    public List<PerformanceReview> getEmployeeReviews(Long employeeId) {
        return reviewRepository.findByEmployeeId(employeeId);
    }

    public List<PerformanceReview> getPendingReviewsForReviewer(Long reviewerId) {
        return reviewRepository.findPendingByReviewer(reviewerId, PerformanceReview.ReviewStatus.DRAFT);
    }

    public List<PerformanceReview> getPendingAcknowledgements(Long employeeId) {
        return reviewRepository.findPendingAcknowledgement(employeeId);
    }

    // Goals
    @Transactional
    public PerformanceGoal createGoal(Long employeeId, String title, String description,
                                      PerformanceGoal.GoalCategory category, LocalDate targetDate, Integer weight) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        PerformanceGoal goal = PerformanceGoal.builder()
                .employee(employee)
                .title(title)
                .description(description)
                .category(category)
                .targetDate(targetDate)
                .weight(weight)
                .startDate(LocalDate.now())
                .status(PerformanceGoal.GoalStatus.NOT_STARTED)
                .build();

        return goalRepository.save(goal);
    }

    @Transactional
    public PerformanceGoal updateGoalProgress(Long goalId, Integer progress, String notes) {
        PerformanceGoal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        goal.setProgressPercentage(progress);
        goal.setNotes(notes);

        if (progress == 100) {
            goal.setStatus(PerformanceGoal.GoalStatus.COMPLETED);
            goal.setCompletedDate(LocalDate.now());
        } else if (progress > 0) {
            goal.setStatus(PerformanceGoal.GoalStatus.IN_PROGRESS);
        }

        return goalRepository.save(goal);
    }

    public List<PerformanceGoal> getEmployeeGoals(Long employeeId) {
        return goalRepository.findByEmployeeId(employeeId);
    }

    public List<PerformanceGoal> getActiveGoals(Long employeeId) {
        return goalRepository.findByEmployeeIdAndStatus(employeeId, PerformanceGoal.GoalStatus.IN_PROGRESS);
    }
}
