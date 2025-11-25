package com.example.alfa.controllers;

import com.example.alfa.models.Document;
import com.example.alfa.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping
    public ResponseEntity<Document> uploadDocument(
            @RequestParam(required = false) Long employeeId,
            @RequestParam Long uploaderId,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam Document.DocumentType type,
            @RequestParam Document.DocumentCategory category,
            @RequestParam String fileName,
            @RequestParam String fileUrl,
            @RequestParam(required = false) String fileType,
            @RequestParam(required = false) Long fileSize,
            @RequestParam(required = false) String effectiveDate,
            @RequestParam(required = false) String expiryDate,
            @RequestParam(defaultValue = "false") boolean isConfidential,
            @RequestParam(defaultValue = "false") boolean requiresAcknowledgement) {
        return ResponseEntity.ok(documentService.uploadDocument(
                employeeId, uploaderId, title, description, type, category,
                fileName, fileUrl, fileType, fileSize,
                effectiveDate != null ? LocalDate.parse(effectiveDate) : null,
                expiryDate != null ? LocalDate.parse(expiryDate) : null,
                isConfidential, requiresAcknowledgement));
    }

    @PostMapping("/{id}/acknowledge/{employeeId}")
    public ResponseEntity<Document> acknowledgeDocument(
            @PathVariable Long id,
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(documentService.acknowledgeDocument(id, employeeId));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Document>> getEmployeeDocuments(@PathVariable Long employeeId) {
        return ResponseEntity.ok(documentService.getEmployeeDocuments(employeeId));
    }

    @GetMapping("/employee/{employeeId}/type/{type}")
    public ResponseEntity<List<Document>> getEmployeeDocumentsByType(
            @PathVariable Long employeeId,
            @PathVariable Document.DocumentType type) {
        return ResponseEntity.ok(documentService.getEmployeeDocumentsByType(employeeId, type));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Document>> getDocumentsByCategory(
            @PathVariable Document.DocumentCategory category) {
        return ResponseEntity.ok(documentService.getDocumentsByCategory(category));
    }

    @GetMapping("/expiring")
    public ResponseEntity<List<Document>> getExpiringDocuments(@RequestParam String beforeDate) {
        return ResponseEntity.ok(documentService.getExpiringDocuments(LocalDate.parse(beforeDate)));
    }

    @GetMapping("/pending-acknowledgement")
    public ResponseEntity<List<Document>> getPendingAcknowledgements() {
        return ResponseEntity.ok(documentService.getPendingAcknowledgements());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateDocument(@PathVariable Long id) {
        documentService.deactivateDocument(id);
        return ResponseEntity.ok().build();
    }
}
