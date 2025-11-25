package com.example.alfa.repositories;

import com.example.alfa.models.Employee;
import com.example.alfa.models.EmployeeTraining;
import com.example.alfa.models.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeTrainingRepository extends JpaRepository<EmployeeTraining, Long> {
    List<EmployeeTraining> findByEmployeeId(Long employeeId);
    List<EmployeeTraining> findByTrainingId(Long trainingId);
    Optional<EmployeeTraining> findByEmployeeAndTraining(Employee employee, Training training);
    List<EmployeeTraining> findByStatus(EmployeeTraining.TrainingStatus status);

    @Query("SELECT et FROM EmployeeTraining et WHERE et.employee.id = :employeeId AND et.status = 'COMPLETED'")
    List<EmployeeTraining> findCompletedByEmployee(Long employeeId);

    List<EmployeeTraining> findByCertificateExpiryDateBefore(LocalDate date);
}
