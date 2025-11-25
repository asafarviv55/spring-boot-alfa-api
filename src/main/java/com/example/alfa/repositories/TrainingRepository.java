package com.example.alfa.repositories;

import com.example.alfa.models.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
    List<Training> findByIsActiveTrue();
    List<Training> findByType(Training.TrainingType type);
    List<Training> findByIsMandatoryTrue();
    List<Training> findByStartDateAfter(LocalDate date);
    List<Training> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
}
