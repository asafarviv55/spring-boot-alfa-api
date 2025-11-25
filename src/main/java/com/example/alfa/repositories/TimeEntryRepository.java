package com.example.alfa.repositories;

import com.example.alfa.models.TimeEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeEntryRepository extends JpaRepository<TimeEntry, Long> {
    List<TimeEntry> findByEmployeeId(Long employeeId);
    List<TimeEntry> findByEmployeeIdAndDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);
    Optional<TimeEntry> findByEmployeeIdAndDate(Long employeeId, LocalDate date);
    List<TimeEntry> findByStatus(TimeEntry.EntryStatus status);

    @Query("SELECT t FROM TimeEntry t WHERE t.employee.manager.id = :managerId AND t.status = :status")
    List<TimeEntry> findByManagerAndStatus(Long managerId, TimeEntry.EntryStatus status);

    @Query("SELECT SUM(t.totalHours) FROM TimeEntry t WHERE t.employee.id = :employeeId AND t.date BETWEEN :startDate AND :endDate AND t.status = 'APPROVED'")
    Double getTotalHours(Long employeeId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(t.overtimeHours) FROM TimeEntry t WHERE t.employee.id = :employeeId AND t.date BETWEEN :startDate AND :endDate AND t.status = 'APPROVED'")
    Double getTotalOvertime(Long employeeId, LocalDate startDate, LocalDate endDate);
}
