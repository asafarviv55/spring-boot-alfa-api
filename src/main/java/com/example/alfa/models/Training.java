package com.example.alfa.models;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trainings")
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrainingType type;

    @Enumerated(EnumType.STRING)
    private TrainingCategory category;

    private String provider;
    private String instructor;
    private String location;

    private LocalDate startDate;
    private LocalDate endDate;
    private Integer durationHours;

    private BigDecimal cost;
    private Integer maxParticipants;

    private Boolean isMandatory = false;
    private Boolean isOnline = false;

    @Column(columnDefinition = "TEXT")
    private String prerequisites;

    @Column(columnDefinition = "TEXT")
    private String objectives;

    @OneToMany(mappedBy = "training")
    private List<EmployeeTraining> enrollments;

    private Boolean isActive = true;

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

    public enum TrainingType {
        ONBOARDING, TECHNICAL, SOFT_SKILLS, COMPLIANCE, CERTIFICATION, LEADERSHIP, SAFETY
    }

    public enum TrainingCategory {
        DEVELOPMENT, COMPLIANCE, MANAGEMENT, TECHNICAL, SOFT_SKILLS
    }
}
