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
@Table(name = "expense_requests")
public class ExpenseRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseCategory category;

    @Column(nullable = false)
    private BigDecimal amount;

    private String currency = "USD";
    private LocalDate expenseDate;

    @Enumerated(EnumType.STRING)
    private ExpenseStatus status = ExpenseStatus.PENDING;

    @OneToMany(mappedBy = "expenseRequest", cascade = CascadeType.ALL)
    private List<ExpenseItem> items;

    private String receiptUrl;
    private String project;
    private String costCenter;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private Employee approvedBy;

    private LocalDateTime approvedAt;

    @Column(columnDefinition = "TEXT")
    private String approverComments;

    private LocalDate reimbursedDate;
    private String reimbursementReference;

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

    public enum ExpenseCategory {
        TRAVEL, MEALS, ACCOMMODATION, SUPPLIES, EQUIPMENT, SOFTWARE, TRAINING, COMMUNICATION, TRANSPORTATION, OTHER
    }

    public enum ExpenseStatus {
        DRAFT, PENDING, APPROVED, REJECTED, REIMBURSED, CANCELLED
    }
}
