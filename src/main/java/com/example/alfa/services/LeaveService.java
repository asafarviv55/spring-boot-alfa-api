package com.example.alfa.services;

import com.example.alfa.models.*;
import com.example.alfa.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeaveService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public LeaveRequest submitLeaveRequest(Long employeeId, LeaveRequest.LeaveType type,
                                           LocalDate startDate, LocalDate endDate, String reason) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        List<LeaveRequest> overlapping = leaveRequestRepository.findOverlappingLeaves(employeeId, startDate, endDate);
        if (!overlapping.isEmpty()) {
            throw new RuntimeException("Leave request overlaps with existing leave");
        }

        double days = endDate.toEpochDay() - startDate.toEpochDay() + 1;
        int year = startDate.getYear();

        LeaveBalance balance = leaveBalanceRepository.findByEmployeeAndLeaveTypeAndYear(employee, type, year)
                .orElseThrow(() -> new RuntimeException("No leave balance found for this type"));

        if (balance.getAvailable() < days) {
            throw new RuntimeException("Insufficient leave balance. Available: " + balance.getAvailable());
        }

        LeaveRequest request = LeaveRequest.builder()
                .employee(employee)
                .leaveType(type)
                .startDate(startDate)
                .endDate(endDate)
                .totalDays(days)
                .reason(reason)
                .status(LeaveRequest.LeaveStatus.PENDING)
                .build();

        balance.setPending(balance.getPending() + days);
        leaveBalanceRepository.save(balance);

        return leaveRequestRepository.save(request);
    }

    @Transactional
    public LeaveRequest approveLeave(Long requestId, Long approverId, String comments) {
        LeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        Employee approver = employeeRepository.findById(approverId)
                .orElseThrow(() -> new RuntimeException("Approver not found"));

        LeaveBalance balance = leaveBalanceRepository.findByEmployeeAndLeaveTypeAndYear(
                request.getEmployee(), request.getLeaveType(), request.getStartDate().getYear())
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));

        balance.setPending(balance.getPending() - request.getTotalDays());
        balance.setUsed(balance.getUsed() + request.getTotalDays());
        leaveBalanceRepository.save(balance);

        request.setStatus(LeaveRequest.LeaveStatus.APPROVED);
        request.setApprovedBy(approver);
        request.setApprovedAt(LocalDateTime.now());
        request.setApproverComments(comments);
        return leaveRequestRepository.save(request);
    }

    @Transactional
    public LeaveRequest rejectLeave(Long requestId, Long approverId, String comments) {
        LeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        Employee approver = employeeRepository.findById(approverId)
                .orElseThrow(() -> new RuntimeException("Approver not found"));

        LeaveBalance balance = leaveBalanceRepository.findByEmployeeAndLeaveTypeAndYear(
                request.getEmployee(), request.getLeaveType(), request.getStartDate().getYear())
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));

        balance.setPending(balance.getPending() - request.getTotalDays());
        leaveBalanceRepository.save(balance);

        request.setStatus(LeaveRequest.LeaveStatus.REJECTED);
        request.setApprovedBy(approver);
        request.setApprovedAt(LocalDateTime.now());
        request.setApproverComments(comments);
        return leaveRequestRepository.save(request);
    }

    @Transactional
    public LeaveRequest cancelLeave(Long requestId) {
        LeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        LeaveBalance balance = leaveBalanceRepository.findByEmployeeAndLeaveTypeAndYear(
                request.getEmployee(), request.getLeaveType(), request.getStartDate().getYear())
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));

        if (request.getStatus() == LeaveRequest.LeaveStatus.APPROVED) {
            balance.setUsed(balance.getUsed() - request.getTotalDays());
        } else if (request.getStatus() == LeaveRequest.LeaveStatus.PENDING) {
            balance.setPending(balance.getPending() - request.getTotalDays());
        }
        leaveBalanceRepository.save(balance);

        request.setStatus(LeaveRequest.LeaveStatus.CANCELLED);
        return leaveRequestRepository.save(request);
    }

    public List<LeaveRequest> getEmployeeLeaves(Long employeeId) {
        return leaveRequestRepository.findByEmployeeId(employeeId);
    }

    public List<LeaveRequest> getPendingApprovals(Long managerId) {
        return leaveRequestRepository.findByManagerIdAndStatus(managerId, LeaveRequest.LeaveStatus.PENDING);
    }

    public List<LeaveBalance> getEmployeeBalances(Long employeeId) {
        return leaveBalanceRepository.findByEmployeeId(employeeId);
    }

    @Transactional
    public LeaveBalance initializeBalance(Long employeeId, LeaveRequest.LeaveType type, int year, double days) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        return leaveBalanceRepository.save(LeaveBalance.builder()
                .employee(employee)
                .leaveType(type)
                .year(year)
                .totalAllotted(days)
                .build());
    }

    public List<LeaveRequest> getTeamLeaveCalendar(Long departmentId, LocalDate date) {
        return leaveRequestRepository.findByDepartmentAndDate(departmentId, date);
    }
}
