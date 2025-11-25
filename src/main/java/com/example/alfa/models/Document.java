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
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType type;

    @Enumerated(EnumType.STRING)
    private DocumentCategory category;

    private String fileName;
    private String fileUrl;
    private String fileType;
    private Long fileSizeBytes;

    private LocalDate effectiveDate;
    private LocalDate expiryDate;

    @ManyToOne
    @JoinColumn(name = "uploaded_by")
    private Employee uploadedBy;

    private Boolean isConfidential = false;
    private Boolean requiresAcknowledgement = false;
    private Boolean isAcknowledged = false;
    private LocalDateTime acknowledgedAt;

    private String version;
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

    public enum DocumentType {
        CONTRACT, OFFER_LETTER, NDA, POLICY, HANDBOOK, ID_PROOF, ADDRESS_PROOF, TAX_FORM, CERTIFICATION, PERFORMANCE_REVIEW, OTHER
    }

    public enum DocumentCategory {
        PERSONAL, EMPLOYMENT, COMPLIANCE, TRAINING, BENEFITS, TAX, POLICY
    }
}
