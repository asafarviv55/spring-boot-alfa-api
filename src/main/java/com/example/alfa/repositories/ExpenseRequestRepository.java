package com.example.alfa.repositories;

import com.example.alfa.models.ExpenseRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRequestRepository extends JpaRepository<ExpenseRequest, Long> {
    List<ExpenseRequest> findByEmployeeId(Long employeeId);
    List<ExpenseRequest> findByStatus(ExpenseRequest.ExpenseStatus status);

    @Query("SELECT e FROM ExpenseRequest e WHERE e.employee.manager.id = :managerId AND e.status = :status")
    List<ExpenseRequest> findByManagerAndStatus(Long managerId, ExpenseRequest.ExpenseStatus status);

    @Query("SELECT SUM(e.amount) FROM ExpenseRequest e WHERE e.employee.id = :employeeId AND e.status = 'APPROVED' AND e.expenseDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalApprovedExpenses(Long employeeId, LocalDate startDate, LocalDate endDate);

    List<ExpenseRequest> findByCategory(ExpenseRequest.ExpenseCategory category);
}
