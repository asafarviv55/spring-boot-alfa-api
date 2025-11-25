package com.example.alfa.models;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "performance_goals")
public class PerformanceGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private PerformanceReview review;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private GoalCategory category = GoalCategory.PERFORMANCE;

    @Enumerated(EnumType.STRING)
    private GoalStatus status = GoalStatus.NOT_STARTED;

    private Integer targetPercentage;
    private Integer progressPercentage = 0;

    private LocalDate startDate;
    private LocalDate targetDate;
    private LocalDate completedDate;

    @Column(columnDefinition = "TEXT")
    private String metrics;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private Integer weight = 1;

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
    }

    public enum GoalCategory {
        PERFORMANCE, DEVELOPMENT, CAREER, PROJECT, TEAM
    }

    public enum GoalStatus {
        NOT_STARTED, IN_PROGRESS, ON_HOLD, COMPLETED, CANCELLED
    }
}
