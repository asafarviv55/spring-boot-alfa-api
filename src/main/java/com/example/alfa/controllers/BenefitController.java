package com.example.alfa.controllers;

import com.example.alfa.models.Benefit;
import com.example.alfa.models.EmployeeBenefit;
import com.example.alfa.services.BenefitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/benefits")
public class BenefitController {

    @Autowired
    private BenefitService benefitService;

    @GetMapping
    public ResponseEntity<List<Benefit>> getAvailableBenefits() {
        return ResponseEntity.ok(benefitService.getAvailableBenefits());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Benefit>> getBenefitsByType(@PathVariable Benefit.BenefitType type) {
        return ResponseEntity.ok(benefitService.getBenefitsByType(type));
    }

    @PostMapping
    public ResponseEntity<Benefit> createBenefit(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam Benefit.BenefitType type,
            @RequestParam(required = false) String provider,
            @RequestParam(required = false) String enrollmentStart,
            @RequestParam(required = false) String enrollmentEnd) {
        return ResponseEntity.ok(benefitService.createBenefit(
                name, description, type, provider,
                enrollmentStart != null ? LocalDate.parse(enrollmentStart) : null,
                enrollmentEnd != null ? LocalDate.parse(enrollmentEnd) : null));
    }

    @PostMapping("/enroll")
    public ResponseEntity<EmployeeBenefit> enrollEmployee(
            @RequestParam Long employeeId,
            @RequestParam Long benefitId,
            @RequestParam(required = false) String planLevel,
            @RequestParam(required = false) String dependents) {
        return ResponseEntity.ok(benefitService.enrollEmployee(employeeId, benefitId, planLevel, dependents));
    }

    @PostMapping("/enrollment/{id}/terminate")
    public ResponseEntity<EmployeeBenefit> terminateEnrollment(
            @PathVariable Long id,
            @RequestParam String terminationDate) {
        return ResponseEntity.ok(benefitService.terminateEnrollment(id, LocalDate.parse(terminationDate)));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeBenefit>> getEmployeeBenefits(@PathVariable Long employeeId) {
        return ResponseEntity.ok(benefitService.getEmployeeBenefits(employeeId));
    }

    @GetMapping("/{benefitId}/enrollments")
    public ResponseEntity<List<EmployeeBenefit>> getBenefitEnrollments(@PathVariable Long benefitId) {
        return ResponseEntity.ok(benefitService.getBenefitEnrollments(benefitId));
    }
}
