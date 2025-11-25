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
@Table(name = "employee_trainings")
public class EmployeeTraining {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(optional = false)
    @JoinColumn(name = "training_id")
    private Training training;

    @Enumerated(EnumType.STRING)
    private TrainingStatus status = TrainingStatus.ENROLLED;

    private LocalDate enrollmentDate;
    private LocalDate completionDate;

    private Integer progressPercentage = 0;
    private Double score;
    private Boolean passed;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    private String certificateUrl;
    private LocalDate certificateExpiryDate;

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

    public enum TrainingStatus {
        ENROLLED, IN_PROGRESS, COMPLETED, FAILED, DROPPED, EXPIRED
    }
}
