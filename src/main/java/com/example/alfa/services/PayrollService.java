package com.example.alfa.services;

import com.example.alfa.models.Employee;
import com.example.alfa.models.Payroll;
import com.example.alfa.repositories.EmployeeRepository;
import com.example.alfa.repositories.PayrollRepository;
import com.example.alfa.repositories.TimeEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PayrollService {

    @Autowired
    private PayrollRepository payrollRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TimeEntryRepository timeEntryRepository;

    @Transactional
    public Payroll generatePayroll(Long employeeId, int month, int year) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        payrollRepository.findByEmployeeAndMonthAndYear(employee, month, year)
                .ifPresent(p -> { throw new RuntimeException("Payroll already exists for this period"); });

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        Double overtimeHours = timeEntryRepository.getTotalOvertime(employeeId, startDate, endDate);
        if (overtimeHours == null) overtimeHours = 0.0;

        BigDecimal hourlyRate = employee.getBaseSalary().divide(BigDecimal.valueOf(176), 2, RoundingMode.HALF_UP);
        BigDecimal overtimePay = hourlyRate.multiply(BigDecimal.valueOf(1.5)).multiply(BigDecimal.valueOf(overtimeHours));

        Payroll payroll = Payroll.builder()
                .employee(employee)
                .month(month)
                .year(year)
                .payPeriodStart(startDate)
                .payPeriodEnd(endDate)
                .baseSalary(employee.getBaseSalary())
                .overtime(overtimePay)
                .status(Payroll.PayrollStatus.CALCULATED)
                .build();

        payroll.calculateTotals();
        return payrollRepository.save(payroll);
    }

    @Transactional
    public Payroll addBonus(Long payrollId, BigDecimal bonus, String notes) {
        Payroll payroll = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new RuntimeException("Payroll not found"));

        payroll.setBonus(payroll.getBonus().add(bonus));
        payroll.setNotes(notes);
        payroll.calculateTotals();
        return payrollRepository.save(payroll);
    }

    @Transactional
    public Payroll addDeduction(Long payrollId, BigDecimal amount, String type) {
        Payroll payroll = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new RuntimeException("Payroll not found"));

        switch (type.toLowerCase()) {
            case "tax":
                payroll.setIncomeTax(payroll.getIncomeTax().add(amount));
                break;
            case "insurance":
                payroll.setHealthInsurance(payroll.getHealthInsurance().add(amount));
                break;
            case "retirement":
                payroll.setRetirement401k(payroll.getRetirement401k().add(amount));
                break;
            default:
                payroll.setOtherDeductions(payroll.getOtherDeductions().add(amount));
        }
        payroll.calculateTotals();
        return payrollRepository.save(payroll);
    }

    @Transactional
    public Payroll approvePayroll(Long payrollId, Long approverId) {
        Payroll payroll = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new RuntimeException("Payroll not found"));

        Employee approver = employeeRepository.findById(approverId)
                .orElseThrow(() -> new RuntimeException("Approver not found"));

        payroll.setStatus(Payroll.PayrollStatus.APPROVED);
        payroll.setProcessedBy(approver);
        payroll.setProcessedAt(LocalDateTime.now());
        return payrollRepository.save(payroll);
    }

    @Transactional
    public Payroll markAsPaid(Long payrollId, LocalDate payDate) {
        Payroll payroll = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new RuntimeException("Payroll not found"));

        payroll.setStatus(Payroll.PayrollStatus.PAID);
        payroll.setPayDate(payDate);
        return payrollRepository.save(payroll);
    }

    public List<Payroll> getEmployeePayrollHistory(Long employeeId) {
        return payrollRepository.findByEmployeeId(employeeId);
    }

    public List<Payroll> getPayrollByPeriod(int month, int year) {
        return payrollRepository.findByMonthAndYear(month, year);
    }

    public List<Payroll> getPendingPayrolls() {
        return payrollRepository.findByStatus(Payroll.PayrollStatus.CALCULATED);
    }

    public BigDecimal calculateTotalPayrollCost(int month, int year) {
        return payrollRepository.findByMonthAndYear(month, year).stream()
                .filter(p -> p.getStatus() != Payroll.PayrollStatus.CANCELLED)
                .map(Payroll::getGrossPay)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
