package com.example.alfa.models;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payroll")
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(nullable = false)
    private Integer month;

    @Column(nullable = false)
    private Integer year;

    private LocalDate payPeriodStart;
    private LocalDate payPeriodEnd;
    private LocalDate payDate;

    // Earnings
    private BigDecimal baseSalary = BigDecimal.ZERO;
    private BigDecimal overtime = BigDecimal.ZERO;
    private BigDecimal bonus = BigDecimal.ZERO;
    private BigDecimal commission = BigDecimal.ZERO;
    private BigDecimal allowances = BigDecimal.ZERO;
    private BigDecimal grossPay = BigDecimal.ZERO;

    // Deductions
    private BigDecimal incomeTax = BigDecimal.ZERO;
    private BigDecimal socialSecurity = BigDecimal.ZERO;
    private BigDecimal healthInsurance = BigDecimal.ZERO;
    private BigDecimal retirement401k = BigDecimal.ZERO;
    private BigDecimal otherDeductions = BigDecimal.ZERO;
    private BigDecimal totalDeductions = BigDecimal.ZERO;

    // Net
    private BigDecimal netPay = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private PayrollStatus status = PayrollStatus.PENDING;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "processed_by")
    private Employee processedBy;

    private LocalDateTime processedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        calculateTotals();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateTotals();
    }

    public void calculateTotals() {
        grossPay = baseSalary.add(overtime).add(bonus).add(commission).add(allowances);
        totalDeductions = incomeTax.add(socialSecurity).add(healthInsurance).add(retirement401k).add(otherDeductions);
        netPay = grossPay.subtract(totalDeductions);
    }

    public enum PayrollStatus {
        PENDING, CALCULATED, APPROVED, PAID, CANCELLED
    }
}
