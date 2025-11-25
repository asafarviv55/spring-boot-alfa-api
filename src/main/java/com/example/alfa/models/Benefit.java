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
@Table(name = "benefits")
public class Benefit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BenefitType type;

    private String provider;
    private BigDecimal employerContribution;
    private BigDecimal employeeContribution;
    private BigDecimal maxCoverage;

    @Column(columnDefinition = "TEXT")
    private String eligibilityCriteria;

    private LocalDate enrollmentStartDate;
    private LocalDate enrollmentEndDate;

    private Boolean isActive = true;

    @OneToMany(mappedBy = "benefit")
    private List<EmployeeBenefit> enrollments;

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

    public enum BenefitType {
        HEALTH_INSURANCE, DENTAL, VISION, LIFE_INSURANCE, DISABILITY, RETIREMENT_401K, HSA, FSA, TUITION_REIMBURSEMENT, GYM_MEMBERSHIP, OTHER
    }
}
