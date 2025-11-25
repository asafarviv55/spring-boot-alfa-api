package com.example.alfa.repositories;

import com.example.alfa.models.PerformanceGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PerformanceGoalRepository extends JpaRepository<PerformanceGoal, Long> {
    List<PerformanceGoal> findByEmployeeId(Long employeeId);
    List<PerformanceGoal> findByReviewId(Long reviewId);
    List<PerformanceGoal> findByEmployeeIdAndStatus(Long employeeId, PerformanceGoal.GoalStatus status);
    List<PerformanceGoal> findByTargetDateBeforeAndStatusNot(LocalDate date, PerformanceGoal.GoalStatus status);
}
