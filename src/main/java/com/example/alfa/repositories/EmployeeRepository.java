package com.example.alfa.repositories;

import com.example.alfa.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);
    Optional<Employee> findByEmployeeNumber(String employeeNumber);
    List<Employee> findByStatus(Employee.EmploymentStatus status);
    List<Employee> findByDepartmentId(Long departmentId);
    List<Employee> findByManagerId(Long managerId);
    List<Employee> findByPositionId(Long positionId);

    @Query("SELECT e FROM Employee e WHERE e.hireDate BETWEEN :startDate AND :endDate")
    List<Employee> findByHireDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT e FROM Employee e WHERE e.status = 'ACTIVE' AND e.department.id = :departmentId")
    List<Employee> findActiveByDepartment(Long departmentId);

    @Query("SELECT COUNT(e) FROM Employee e WHERE e.status = 'ACTIVE'")
    long countActiveEmployees();
}
