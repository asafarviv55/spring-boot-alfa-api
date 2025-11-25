package com.example.alfa.repositories;

import com.example.alfa.models.Employee;
import com.example.alfa.models.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    List<Payroll> findByEmployeeId(Long employeeId);
    List<Payroll> findByMonthAndYear(Integer month, Integer year);
    Optional<Payroll> findByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year);
    List<Payroll> findByStatus(Payroll.PayrollStatus status);

    @Query("SELECT p FROM Payroll p WHERE p.employee.department.id = :departmentId AND p.month = :month AND p.year = :year")
    List<Payroll> findByDepartmentAndPeriod(Long departmentId, Integer month, Integer year);
}
