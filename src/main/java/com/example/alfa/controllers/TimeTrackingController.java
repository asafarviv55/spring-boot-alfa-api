package com.example.alfa.controllers;

import com.example.alfa.models.TimeEntry;
import com.example.alfa.services.TimeTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/time")
public class TimeTrackingController {

    @Autowired
    private TimeTrackingService timeTrackingService;

    @PostMapping("/clock-in/{employeeId}")
    public ResponseEntity<TimeEntry> clockIn(
            @PathVariable Long employeeId,
            @RequestParam(required = false) String project,
            @RequestParam(required = false) String task) {
        return ResponseEntity.ok(timeTrackingService.clockIn(employeeId, project, task));
    }

    @PostMapping("/clock-out/{employeeId}")
    public ResponseEntity<TimeEntry> clockOut(@PathVariable Long employeeId) {
        return ResponseEntity.ok(timeTrackingService.clockOut(employeeId));
    }

    @PostMapping("/break/start/{employeeId}")
    public ResponseEntity<TimeEntry> startBreak(@PathVariable Long employeeId) {
        return ResponseEntity.ok(timeTrackingService.startBreak(employeeId));
    }

    @PostMapping("/break/end/{employeeId}")
    public ResponseEntity<TimeEntry> endBreak(@PathVariable Long employeeId) {
        return ResponseEntity.ok(timeTrackingService.endBreak(employeeId));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<TimeEntry> approveTimeEntry(
            @PathVariable Long id,
            @RequestParam Long approverId) {
        return ResponseEntity.ok(timeTrackingService.approveTimeEntry(id, approverId));
    }

    @GetMapping("/timesheet/{employeeId}")
    public ResponseEntity<List<TimeEntry>> getEmployeeTimesheet(
            @PathVariable Long employeeId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return ResponseEntity.ok(timeTrackingService.getEmployeeTimesheet(
                employeeId, LocalDate.parse(startDate), LocalDate.parse(endDate)));
    }

    @GetMapping("/pending/manager/{managerId}")
    public ResponseEntity<List<TimeEntry>> getPendingApprovals(@PathVariable Long managerId) {
        return ResponseEntity.ok(timeTrackingService.getPendingApprovals(managerId));
    }

    @GetMapping("/summary/{employeeId}")
    public ResponseEntity<Map<String, Object>> getTimeSummary(
            @PathVariable Long employeeId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return ResponseEntity.ok(timeTrackingService.getTimeSummary(
                employeeId, LocalDate.parse(startDate), LocalDate.parse(endDate)));
    }
}
