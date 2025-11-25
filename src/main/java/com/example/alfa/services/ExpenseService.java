package com.example.alfa.services;

import com.example.alfa.models.Employee;
import com.example.alfa.models.ExpenseRequest;
import com.example.alfa.models.ExpenseItem;
import com.example.alfa.repositories.EmployeeRepository;
import com.example.alfa.repositories.ExpenseRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRequestRepository expenseRequestRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public ExpenseRequest createExpenseRequest(Long employeeId, String title, String description,
                                               ExpenseRequest.ExpenseCategory category, BigDecimal amount,
                                               LocalDate expenseDate, String project) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        ExpenseRequest request = ExpenseRequest.builder()
                .employee(employee)
                .title(title)
                .description(description)
                .category(category)
                .amount(amount)
                .expenseDate(expenseDate)
                .project(project)
                .status(ExpenseRequest.ExpenseStatus.PENDING)
                .build();

        return expenseRequestRepository.save(request);
    }

    @Transactional
    public ExpenseRequest submitForApproval(Long requestId) {
        ExpenseRequest request = expenseRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Expense request not found"));

        if (request.getStatus() != ExpenseRequest.ExpenseStatus.DRAFT) {
            throw new RuntimeException("Only draft expenses can be submitted");
        }

        request.setStatus(ExpenseRequest.ExpenseStatus.PENDING);
        return expenseRequestRepository.save(request);
    }

    @Transactional
    public ExpenseRequest approveExpense(Long requestId, Long approverId, String comments) {
        ExpenseRequest request = expenseRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Expense request not found"));

        Employee approver = employeeRepository.findById(approverId)
                .orElseThrow(() -> new RuntimeException("Approver not found"));

        request.setStatus(ExpenseRequest.ExpenseStatus.APPROVED);
        request.setApprovedBy(approver);
        request.setApprovedAt(LocalDateTime.now());
        request.setApproverComments(comments);
        return expenseRequestRepository.save(request);
    }

    @Transactional
    public ExpenseRequest rejectExpense(Long requestId, Long approverId, String comments) {
        ExpenseRequest request = expenseRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Expense request not found"));

        Employee approver = employeeRepository.findById(approverId)
                .orElseThrow(() -> new RuntimeException("Approver not found"));

        request.setStatus(ExpenseRequest.ExpenseStatus.REJECTED);
        request.setApprovedBy(approver);
        request.setApprovedAt(LocalDateTime.now());
        request.setApproverComments(comments);
        return expenseRequestRepository.save(request);
    }

    @Transactional
    public ExpenseRequest markAsReimbursed(Long requestId, String reference) {
        ExpenseRequest request = expenseRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Expense request not found"));

        request.setStatus(ExpenseRequest.ExpenseStatus.REIMBURSED);
        request.setReimbursedDate(LocalDate.now());
        request.setReimbursementReference(reference);
        return expenseRequestRepository.save(request);
    }

    public List<ExpenseRequest> getEmployeeExpenses(Long employeeId) {
        return expenseRequestRepository.findByEmployeeId(employeeId);
    }

    public List<ExpenseRequest> getPendingApprovals(Long managerId) {
        return expenseRequestRepository.findByManagerAndStatus(managerId, ExpenseRequest.ExpenseStatus.PENDING);
    }

    public BigDecimal getTotalExpenses(Long employeeId, LocalDate startDate, LocalDate endDate) {
        BigDecimal total = expenseRequestRepository.getTotalApprovedExpenses(employeeId, startDate, endDate);
        return total != null ? total : BigDecimal.ZERO;
    }
}
