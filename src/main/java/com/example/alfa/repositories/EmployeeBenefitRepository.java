package com.example.alfa.repositories;

import com.example.alfa.models.Employee;
import com.example.alfa.models.EmployeeBenefit;
import com.example.alfa.models.Benefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeBenefitRepository extends JpaRepository<EmployeeBenefit, Long> {
    List<EmployeeBenefit> findByEmployeeId(Long employeeId);
    List<EmployeeBenefit> findByBenefitId(Long benefitId);
    Optional<EmployeeBenefit> findByEmployeeAndBenefit(Employee employee, Benefit benefit);
    List<EmployeeBenefit> findByStatus(EmployeeBenefit.EnrollmentStatus status);
}
