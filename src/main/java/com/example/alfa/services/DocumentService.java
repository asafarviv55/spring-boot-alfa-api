package com.example.alfa.services;

import com.example.alfa.models.Document;
import com.example.alfa.models.Employee;
import com.example.alfa.repositories.DocumentRepository;
import com.example.alfa.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public Document uploadDocument(Long employeeId, Long uploaderId, String title, String description,
                                   Document.DocumentType type, Document.DocumentCategory category,
                                   String fileName, String fileUrl, String fileType, Long fileSize,
                                   LocalDate effectiveDate, LocalDate expiryDate, boolean isConfidential,
                                   boolean requiresAcknowledgement) {
        Employee employee = employeeId != null ? employeeRepository.findById(employeeId).orElse(null) : null;
        Employee uploader = employeeRepository.findById(uploaderId)
                .orElseThrow(() -> new RuntimeException("Uploader not found"));

        Document document = Document.builder()
                .employee(employee)
                .uploadedBy(uploader)
                .title(title)
                .description(description)
                .type(type)
                .category(category)
                .fileName(fileName)
                .fileUrl(fileUrl)
                .fileType(fileType)
                .fileSizeBytes(fileSize)
                .effectiveDate(effectiveDate)
                .expiryDate(expiryDate)
                .isConfidential(isConfidential)
                .requiresAcknowledgement(requiresAcknowledgement)
                .isActive(true)
                .build();

        return documentRepository.save(document);
    }

    @Transactional
    public Document acknowledgeDocument(Long documentId, Long employeeId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        if (!document.getRequiresAcknowledgement()) {
            throw new RuntimeException("This document does not require acknowledgement");
        }

        if (document.getEmployee() != null && !document.getEmployee().getId().equals(employeeId)) {
            throw new RuntimeException("You can only acknowledge your own documents");
        }

        document.setIsAcknowledged(true);
        document.setAcknowledgedAt(LocalDateTime.now());
        return documentRepository.save(document);
    }

    public List<Document> getEmployeeDocuments(Long employeeId) {
        return documentRepository.findByEmployeeId(employeeId);
    }

    public List<Document> getEmployeeDocumentsByType(Long employeeId, Document.DocumentType type) {
        return documentRepository.findByEmployeeIdAndType(employeeId, type);
    }

    public List<Document> getDocumentsByCategory(Document.DocumentCategory category) {
        return documentRepository.findByCategory(category);
    }

    public List<Document> getExpiringDocuments(LocalDate beforeDate) {
        return documentRepository.findByExpiryDateBefore(beforeDate);
    }

    public List<Document> getPendingAcknowledgements() {
        return documentRepository.findByRequiresAcknowledgementTrueAndIsAcknowledgedFalse();
    }

    @Transactional
    public void deactivateDocument(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        document.setIsActive(false);
        documentRepository.save(document);
    }
}
