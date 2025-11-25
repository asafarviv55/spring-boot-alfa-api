package com.example.alfa.controllers;

import com.example.alfa.models.Payroll;
import com.example.alfa.services.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;

    @PostMapping("/generate/{employeeId}")
    public ResponseEntity<Payroll> generatePayroll(
            @PathVariable Long employeeId,
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(payrollService.generatePayroll(employeeId, month, year));
    }

    @PostMapping("/{id}/bonus")
    public ResponseEntity<Payroll> addBonus(
            @PathVariable Long id,
            @RequestParam BigDecimal bonus,
            @RequestParam(required = false) String notes) {
        return ResponseEntity.ok(payrollService.addBonus(id, bonus, notes));
    }

    @PostMapping("/{id}/deduction")
    public ResponseEntity<Payroll> addDeduction(
            @PathVariable Long id,
            @RequestParam BigDecimal amount,
            @RequestParam String type) {
        return ResponseEntity.ok(payrollService.addDeduction(id, amount, type));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Payroll> approvePayroll(
            @PathVariable Long id,
            @RequestParam Long approverId) {
        return ResponseEntity.ok(payrollService.approvePayroll(id, approverId));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<Payroll> markAsPaid(
            @PathVariable Long id,
            @RequestParam String payDate) {
        return ResponseEntity.ok(payrollService.markAsPaid(id, LocalDate.parse(payDate)));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Payroll>> getEmployeePayrollHistory(@PathVariable Long employeeId) {
        return ResponseEntity.ok(payrollService.getEmployeePayrollHistory(employeeId));
    }

    @GetMapping("/period")
    public ResponseEntity<List<Payroll>> getPayrollByPeriod(
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(payrollService.getPayrollByPeriod(month, year));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Payroll>> getPendingPayrolls() {
        return ResponseEntity.ok(payrollService.getPendingPayrolls());
    }

    @GetMapping("/total-cost")
    public ResponseEntity<Map<String, Object>> getTotalPayrollCost(
            @RequestParam int month,
            @RequestParam int year) {
        BigDecimal total = payrollService.calculateTotalPayrollCost(month, year);
        return ResponseEntity.ok(Map.of("month", month, "year", year, "totalCost", total));
    }
}
