package com.example.alfa.services;

import com.example.alfa.models.*;
import com.example.alfa.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BenefitService {

    @Autowired
    private BenefitRepository benefitRepository;

    @Autowired
    private EmployeeBenefitRepository employeeBenefitRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Benefit> getAvailableBenefits() {
        return benefitRepository.findByIsActiveTrue();
    }

    public List<Benefit> getBenefitsByType(Benefit.BenefitType type) {
        return benefitRepository.findByType(type);
    }

    @Transactional
    public Benefit createBenefit(String name, String description, Benefit.BenefitType type,
                                 String provider, LocalDate enrollmentStart, LocalDate enrollmentEnd) {
        Benefit benefit = Benefit.builder()
                .name(name)
                .description(description)
                .type(type)
                .provider(provider)
                .enrollmentStartDate(enrollmentStart)
                .enrollmentEndDate(enrollmentEnd)
                .isActive(true)
                .build();

        return benefitRepository.save(benefit);
    }

    @Transactional
    public EmployeeBenefit enrollEmployee(Long employeeId, Long benefitId, String planLevel, String dependents) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Benefit benefit = benefitRepository.findById(benefitId)
                .orElseThrow(() -> new RuntimeException("Benefit not found"));

        if (!benefit.getIsActive()) {
            throw new RuntimeException("Benefit is not active");
        }

        LocalDate now = LocalDate.now();
        if (benefit.getEnrollmentEndDate() != null && now.isAfter(benefit.getEnrollmentEndDate())) {
            throw new RuntimeException("Enrollment period has ended");
        }

        employeeBenefitRepository.findByEmployeeAndBenefit(employee, benefit)
                .ifPresent(eb -> { throw new RuntimeException("Employee already enrolled in this benefit"); });

        EmployeeBenefit enrollment = EmployeeBenefit.builder()
                .employee(employee)
                .benefit(benefit)
                .enrollmentDate(now)
                .effectiveDate(now.plusDays(1).withDayOfMonth(1))
                .planLevel(planLevel)
                .dependentsCovered(dependents)
                .status(EmployeeBenefit.EnrollmentStatus.ACTIVE)
                .build();

        return employeeBenefitRepository.save(enrollment);
    }

    @Transactional
    public EmployeeBenefit terminateEnrollment(Long enrollmentId, LocalDate terminationDate) {
        EmployeeBenefit enrollment = employeeBenefitRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollment.setStatus(EmployeeBenefit.EnrollmentStatus.TERMINATED);
        enrollment.setTerminationDate(terminationDate);
        return employeeBenefitRepository.save(enrollment);
    }

    public List<EmployeeBenefit> getEmployeeBenefits(Long employeeId) {
        return employeeBenefitRepository.findByEmployeeId(employeeId);
    }

    public List<EmployeeBenefit> getBenefitEnrollments(Long benefitId) {
        return employeeBenefitRepository.findByBenefitId(benefitId);
    }
}
