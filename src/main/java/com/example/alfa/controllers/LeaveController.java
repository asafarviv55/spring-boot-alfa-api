package com.example.alfa.controllers;

import com.example.alfa.models.LeaveBalance;
import com.example.alfa.models.LeaveRequest;
import com.example.alfa.services.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/leave")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @PostMapping("/request")
    public ResponseEntity<LeaveRequest> submitLeaveRequest(
            @RequestParam Long employeeId,
            @RequestParam LeaveRequest.LeaveType type,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(leaveService.submitLeaveRequest(
                employeeId, type, LocalDate.parse(startDate), LocalDate.parse(endDate), reason));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<LeaveRequest> approveLeave(
            @PathVariable Long id,
            @RequestParam Long approverId,
            @RequestParam(required = false) String comments) {
        return ResponseEntity.ok(leaveService.approveLeave(id, approverId, comments));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<LeaveRequest> rejectLeave(
            @PathVariable Long id,
            @RequestParam Long approverId,
            @RequestParam(required = false) String comments) {
        return ResponseEntity.ok(leaveService.rejectLeave(id, approverId, comments));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<LeaveRequest> cancelLeave(@PathVariable Long id) {
        return ResponseEntity.ok(leaveService.cancelLeave(id));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<LeaveRequest>> getEmployeeLeaves(@PathVariable Long employeeId) {
        return ResponseEntity.ok(leaveService.getEmployeeLeaves(employeeId));
    }

    @GetMapping("/pending/manager/{managerId}")
    public ResponseEntity<List<LeaveRequest>> getPendingApprovals(@PathVariable Long managerId) {
        return ResponseEntity.ok(leaveService.getPendingApprovals(managerId));
    }

    @GetMapping("/balance/{employeeId}")
    public ResponseEntity<List<LeaveBalance>> getEmployeeBalances(@PathVariable Long employeeId) {
        return ResponseEntity.ok(leaveService.getEmployeeBalances(employeeId));
    }

    @PostMapping("/balance/initialize")
    public ResponseEntity<LeaveBalance> initializeBalance(
            @RequestParam Long employeeId,
            @RequestParam LeaveRequest.LeaveType type,
            @RequestParam int year,
            @RequestParam double days) {
        return ResponseEntity.ok(leaveService.initializeBalance(employeeId, type, year, days));
    }

    @GetMapping("/calendar/department/{departmentId}")
    public ResponseEntity<List<LeaveRequest>> getTeamLeaveCalendar(
            @PathVariable Long departmentId,
            @RequestParam String date) {
        return ResponseEntity.ok(leaveService.getTeamLeaveCalendar(departmentId, LocalDate.parse(date)));
    }
}
