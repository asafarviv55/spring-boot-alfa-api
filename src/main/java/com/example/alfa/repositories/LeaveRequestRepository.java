package com.example.alfa.repositories;

import com.example.alfa.models.Employee;
import com.example.alfa.models.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployeeId(Long employeeId);
    List<LeaveRequest> findByEmployeeAndStatus(Employee employee, LeaveRequest.LeaveStatus status);
    List<LeaveRequest> findByStatus(LeaveRequest.LeaveStatus status);

    @Query("SELECT l FROM LeaveRequest l WHERE l.employee.manager.id = :managerId AND l.status = :status")
    List<LeaveRequest> findByManagerIdAndStatus(Long managerId, LeaveRequest.LeaveStatus status);

    @Query("SELECT l FROM LeaveRequest l WHERE l.employee.id = :employeeId AND " +
           "((l.startDate <= :endDate AND l.endDate >= :startDate))")
    List<LeaveRequest> findOverlappingLeaves(Long employeeId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT l FROM LeaveRequest l WHERE l.employee.department.id = :departmentId AND " +
           "l.startDate <= :date AND l.endDate >= :date AND l.status = 'APPROVED'")
    List<LeaveRequest> findByDepartmentAndDate(Long departmentId, LocalDate date);
}
