package com.example.alfa.controllers;

import com.example.alfa.models.EmployeeTraining;
import com.example.alfa.models.Training;
import com.example.alfa.services.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/training")
public class TrainingController {

    @Autowired
    private TrainingService trainingService;

    @GetMapping
    public ResponseEntity<List<Training>> getAvailableTrainings() {
        return ResponseEntity.ok(trainingService.getAvailableTrainings());
    }

    @GetMapping("/mandatory")
    public ResponseEntity<List<Training>> getMandatoryTrainings() {
        return ResponseEntity.ok(trainingService.getMandatoryTrainings());
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Training>> getUpcomingTrainings() {
        return ResponseEntity.ok(trainingService.getUpcomingTrainings());
    }

    @PostMapping
    public ResponseEntity<Training> createTraining(
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam Training.TrainingType type,
            @RequestParam(required = false) String provider,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam int durationHours,
            @RequestParam(defaultValue = "false") boolean isMandatory) {
        return ResponseEntity.ok(trainingService.createTraining(
                title, description, type, provider,
                LocalDate.parse(startDate), LocalDate.parse(endDate),
                durationHours, isMandatory));
    }

    @PostMapping("/{trainingId}/enroll/{employeeId}")
    public ResponseEntity<EmployeeTraining> enrollEmployee(
            @PathVariable Long trainingId,
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(trainingService.enrollEmployee(employeeId, trainingId));
    }

    @PutMapping("/enrollment/{id}/progress")
    public ResponseEntity<EmployeeTraining> updateProgress(
            @PathVariable Long id,
            @RequestParam int progress) {
        return ResponseEntity.ok(trainingService.updateProgress(id, progress));
    }

    @PostMapping("/enrollment/{id}/complete")
    public ResponseEntity<EmployeeTraining> completeTraining(
            @PathVariable Long id,
            @RequestParam double score,
            @RequestParam boolean passed,
            @RequestParam(required = false) String certificateUrl) {
        return ResponseEntity.ok(trainingService.completeTraining(id, score, passed, certificateUrl));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeTraining>> getEmployeeTrainings(@PathVariable Long employeeId) {
        return ResponseEntity.ok(trainingService.getEmployeeTrainings(employeeId));
    }

    @GetMapping("/employee/{employeeId}/completed")
    public ResponseEntity<List<EmployeeTraining>> getCompletedTrainings(@PathVariable Long employeeId) {
        return ResponseEntity.ok(trainingService.getCompletedTrainings(employeeId));
    }

    @GetMapping("/certifications/expiring")
    public ResponseEntity<List<EmployeeTraining>> getExpiringCertifications(
            @RequestParam String beforeDate) {
        return ResponseEntity.ok(trainingService.getExpiringCertifications(LocalDate.parse(beforeDate)));
    }
}
