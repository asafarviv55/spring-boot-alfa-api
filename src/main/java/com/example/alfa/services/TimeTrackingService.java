package com.example.alfa.services;

import com.example.alfa.models.Employee;
import com.example.alfa.models.TimeEntry;
import com.example.alfa.repositories.EmployeeRepository;
import com.example.alfa.repositories.TimeEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TimeTrackingService {

    private static final LocalTime STANDARD_START_TIME = LocalTime.of(9, 0);

    @Autowired
    private TimeEntryRepository timeEntryRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public TimeEntry clockIn(Long employeeId, String project, String task) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LocalDate today = LocalDate.now();
        timeEntryRepository.findByEmployeeIdAndDate(employeeId, today)
                .ifPresent(e -> { throw new RuntimeException("Already clocked in today"); });

        LocalTime now = LocalTime.now();
        boolean isLate = now.isAfter(STANDARD_START_TIME.plusMinutes(15));

        TimeEntry entry = TimeEntry.builder()
                .employee(employee)
                .date(today)
                .clockIn(now)
                .project(project)
                .taskDescription(task)
                .type(TimeEntry.EntryType.REGULAR)
                .isLate(isLate)
                .build();

        return timeEntryRepository.save(entry);
    }

    @Transactional
    public TimeEntry clockOut(Long employeeId) {
        TimeEntry entry = timeEntryRepository.findByEmployeeIdAndDate(employeeId, LocalDate.now())
                .orElseThrow(() -> new RuntimeException("No clock-in record found for today"));

        if (entry.getClockOut() != null) {
            throw new RuntimeException("Already clocked out");
        }

        LocalTime now = LocalTime.now();
        entry.setClockOut(now);
        entry.setIsEarlyLeave(now.isBefore(LocalTime.of(17, 0)));
        entry.calculateHours();

        return timeEntryRepository.save(entry);
    }

    @Transactional
    public TimeEntry startBreak(Long employeeId) {
        TimeEntry entry = timeEntryRepository.findByEmployeeIdAndDate(employeeId, LocalDate.now())
                .orElseThrow(() -> new RuntimeException("No clock-in record found"));

        entry.setBreakStart(LocalTime.now());
        return timeEntryRepository.save(entry);
    }

    @Transactional
    public TimeEntry endBreak(Long employeeId) {
        TimeEntry entry = timeEntryRepository.findByEmployeeIdAndDate(employeeId, LocalDate.now())
                .orElseThrow(() -> new RuntimeException("No clock-in record found"));

        entry.setBreakEnd(LocalTime.now());
        return timeEntryRepository.save(entry);
    }

    @Transactional
    public TimeEntry approveTimeEntry(Long entryId, Long approverId) {
        TimeEntry entry = timeEntryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Time entry not found"));

        Employee approver = employeeRepository.findById(approverId)
                .orElseThrow(() -> new RuntimeException("Approver not found"));

        entry.setStatus(TimeEntry.EntryStatus.APPROVED);
        entry.setApprovedBy(approver);
        entry.setApprovedAt(LocalDateTime.now());
        return timeEntryRepository.save(entry);
    }

    public List<TimeEntry> getEmployeeTimesheet(Long employeeId, LocalDate startDate, LocalDate endDate) {
        return timeEntryRepository.findByEmployeeIdAndDateBetween(employeeId, startDate, endDate);
    }

    public List<TimeEntry> getPendingApprovals(Long managerId) {
        return timeEntryRepository.findByManagerAndStatus(managerId, TimeEntry.EntryStatus.PENDING);
    }

    public Map<String, Object> getTimeSummary(Long employeeId, LocalDate startDate, LocalDate endDate) {
        Double totalHours = timeEntryRepository.getTotalHours(employeeId, startDate, endDate);
        Double overtimeHours = timeEntryRepository.getTotalOvertime(employeeId, startDate, endDate);

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalHours", totalHours != null ? totalHours : 0);
        summary.put("overtimeHours", overtimeHours != null ? overtimeHours : 0);
        summary.put("regularHours", (totalHours != null ? totalHours : 0) - (overtimeHours != null ? overtimeHours : 0));
        summary.put("startDate", startDate);
        summary.put("endDate", endDate);
        return summary;
    }
}
