package com.example.alfa.models;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "performance_reviews")
public class PerformanceReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reviewer_id")
    private Employee reviewer;

    @Column(nullable = false)
    private String reviewPeriod;

    private LocalDate reviewDate;
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private ReviewType reviewType = ReviewType.ANNUAL;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status = ReviewStatus.DRAFT;

    // Ratings (1-5 scale)
    private Integer performanceRating;
    private Integer communicationRating;
    private Integer teamworkRating;
    private Integer leadershipRating;
    private Integer innovationRating;
    private Integer attendanceRating;
    private Double overallRating;

    @Column(columnDefinition = "TEXT")
    private String strengths;

    @Column(columnDefinition = "TEXT")
    private String areasForImprovement;

    @Column(columnDefinition = "TEXT")
    private String achievements;

    @Column(columnDefinition = "TEXT")
    private String goals;

    @Column(columnDefinition = "TEXT")
    private String reviewerComments;

    @Column(columnDefinition = "TEXT")
    private String employeeComments;

    private Boolean employeeAcknowledged = false;
    private LocalDateTime acknowledgedAt;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<PerformanceGoal> performanceGoals;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateOverallRating();
    }

    public void calculateOverallRating() {
        int count = 0;
        double total = 0;

        if (performanceRating != null) { total += performanceRating; count++; }
        if (communicationRating != null) { total += communicationRating; count++; }
        if (teamworkRating != null) { total += teamworkRating; count++; }
        if (leadershipRating != null) { total += leadershipRating; count++; }
        if (innovationRating != null) { total += innovationRating; count++; }
        if (attendanceRating != null) { total += attendanceRating; count++; }

        overallRating = count > 0 ? Math.round(total / count * 10.0) / 10.0 : null;
    }

    public enum ReviewType {
        ANNUAL, SEMI_ANNUAL, QUARTERLY, PROBATION, PROJECT, PROMOTION
    }

    public enum ReviewStatus {
        DRAFT, PENDING_REVIEW, PENDING_ACKNOWLEDGEMENT, COMPLETED, CANCELLED
    }
}
