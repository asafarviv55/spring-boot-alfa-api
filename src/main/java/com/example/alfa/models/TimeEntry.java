package com.example.alfa.models;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "time_entries")
public class TimeEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(nullable = false)
    private LocalDate date;

    private LocalTime clockIn;
    private LocalTime clockOut;
    private LocalTime breakStart;
    private LocalTime breakEnd;

    private Double regularHours = 0.0;
    private Double overtimeHours = 0.0;
    private Double breakDuration = 0.0;
    private Double totalHours = 0.0;

    @Enumerated(EnumType.STRING)
    private EntryStatus status = EntryStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private EntryType type = EntryType.REGULAR;

    private String notes;
    private String project;
    private String taskDescription;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private Employee approvedBy;

    private LocalDateTime approvedAt;
    private Boolean isLate = false;
    private Boolean isEarlyLeave = false;

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
        calculateHours();
    }

    public void calculateHours() {
        if (clockIn != null && clockOut != null) {
            Duration duration = Duration.between(clockIn, clockOut);
            double hours = duration.toMinutes() / 60.0;

            if (breakStart != null && breakEnd != null) {
                Duration breakDur = Duration.between(breakStart, breakEnd);
                breakDuration = breakDur.toMinutes() / 60.0;
                hours -= breakDuration;
            }

            totalHours = Math.round(hours * 100.0) / 100.0;
            regularHours = Math.min(totalHours, 8.0);
            overtimeHours = Math.max(0, totalHours - 8.0);
        }
    }

    public enum EntryStatus {
        PENDING, APPROVED, REJECTED
    }

    public enum EntryType {
        REGULAR, REMOTE, SICK, VACATION, HOLIDAY
    }
}
