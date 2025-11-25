package com.example.alfa.repositories;

import com.example.alfa.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByEmployeeId(Long employeeId);
    List<Document> findByType(Document.DocumentType type);
    List<Document> findByCategory(Document.DocumentCategory category);
    List<Document> findByEmployeeIdAndType(Long employeeId, Document.DocumentType type);
    List<Document> findByExpiryDateBefore(LocalDate date);
    List<Document> findByRequiresAcknowledgementTrueAndIsAcknowledgedFalse();
}
