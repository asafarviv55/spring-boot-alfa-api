package com.example.alfa.controllers;

import com.example.alfa.models.ExpenseRequest;
import com.example.alfa.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseRequest> createExpenseRequest(
            @RequestParam Long employeeId,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam ExpenseRequest.ExpenseCategory category,
            @RequestParam BigDecimal amount,
            @RequestParam String expenseDate,
            @RequestParam(required = false) String project) {
        return ResponseEntity.ok(expenseService.createExpenseRequest(
                employeeId, title, description, category, amount, LocalDate.parse(expenseDate), project));
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<ExpenseRequest> submitForApproval(@PathVariable Long id) {
        return ResponseEntity.ok(expenseService.submitForApproval(id));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ExpenseRequest> approveExpense(
            @PathVariable Long id,
            @RequestParam Long approverId,
            @RequestParam(required = false) String comments) {
        return ResponseEntity.ok(expenseService.approveExpense(id, approverId, comments));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ExpenseRequest> rejectExpense(
            @PathVariable Long id,
            @RequestParam Long approverId,
            @RequestParam(required = false) String comments) {
        return ResponseEntity.ok(expenseService.rejectExpense(id, approverId, comments));
    }

    @PostMapping("/{id}/reimburse")
    public ResponseEntity<ExpenseRequest> markAsReimbursed(
            @PathVariable Long id,
            @RequestParam String reference) {
        return ResponseEntity.ok(expenseService.markAsReimbursed(id, reference));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<ExpenseRequest>> getEmployeeExpenses(@PathVariable Long employeeId) {
        return ResponseEntity.ok(expenseService.getEmployeeExpenses(employeeId));
    }

    @GetMapping("/pending/manager/{managerId}")
    public ResponseEntity<List<ExpenseRequest>> getPendingApprovals(@PathVariable Long managerId) {
        return ResponseEntity.ok(expenseService.getPendingApprovals(managerId));
    }

    @GetMapping("/total/{employeeId}")
    public ResponseEntity<Map<String, Object>> getTotalExpenses(
            @PathVariable Long employeeId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        BigDecimal total = expenseService.getTotalExpenses(
                employeeId, LocalDate.parse(startDate), LocalDate.parse(endDate));
        return ResponseEntity.ok(Map.of("employeeId", employeeId, "total", total,
                "startDate", startDate, "endDate", endDate));
    }
}
