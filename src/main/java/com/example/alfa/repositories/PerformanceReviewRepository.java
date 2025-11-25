package com.example.alfa.repositories;

import com.example.alfa.models.PerformanceReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Long> {
    List<PerformanceReview> findByEmployeeId(Long employeeId);
    List<PerformanceReview> findByReviewerId(Long reviewerId);
    List<PerformanceReview> findByStatus(PerformanceReview.ReviewStatus status);
    List<PerformanceReview> findByReviewPeriod(String reviewPeriod);

    @Query("SELECT r FROM PerformanceReview r WHERE r.reviewer.id = :reviewerId AND r.status = :status")
    List<PerformanceReview> findPendingByReviewer(Long reviewerId, PerformanceReview.ReviewStatus status);

    @Query("SELECT r FROM PerformanceReview r WHERE r.employee.id = :employeeId AND r.status = 'PENDING_ACKNOWLEDGEMENT'")
    List<PerformanceReview> findPendingAcknowledgement(Long employeeId);

    List<PerformanceReview> findByDueDateBeforeAndStatusIn(LocalDate date, List<PerformanceReview.ReviewStatus> statuses);
}
