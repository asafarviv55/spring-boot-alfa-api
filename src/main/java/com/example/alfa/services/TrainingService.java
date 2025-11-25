package com.example.alfa.services;

import com.example.alfa.models.*;
import com.example.alfa.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TrainingService {

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private EmployeeTrainingRepository employeeTrainingRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public Training createTraining(String title, String description, Training.TrainingType type,
                                   String provider, LocalDate startDate, LocalDate endDate,
                                   int durationHours, boolean isMandatory) {
        Training training = Training.builder()
                .title(title)
                .description(description)
                .type(type)
                .provider(provider)
                .startDate(startDate)
                .endDate(endDate)
                .durationHours(durationHours)
                .isMandatory(isMandatory)
                .isActive(true)
                .build();

        return trainingRepository.save(training);
    }

    public List<Training> getAvailableTrainings() {
        return trainingRepository.findByIsActiveTrue();
    }

    public List<Training> getMandatoryTrainings() {
        return trainingRepository.findByIsMandatoryTrue();
    }

    public List<Training> getUpcomingTrainings() {
        return trainingRepository.findByStartDateAfter(LocalDate.now());
    }

    @Transactional
    public EmployeeTraining enrollEmployee(Long employeeId, Long trainingId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new RuntimeException("Training not found"));

        employeeTrainingRepository.findByEmployeeAndTraining(employee, training)
                .ifPresent(et -> { throw new RuntimeException("Employee already enrolled in this training"); });

        if (training.getMaxParticipants() != null) {
            long currentEnrollments = employeeTrainingRepository.findByTrainingId(trainingId).stream()
                    .filter(et -> et.getStatus() != EmployeeTraining.TrainingStatus.DROPPED)
                    .count();
            if (currentEnrollments >= training.getMaxParticipants()) {
                throw new RuntimeException("Training is full");
            }
        }

        EmployeeTraining enrollment = EmployeeTraining.builder()
                .employee(employee)
                .training(training)
                .enrollmentDate(LocalDate.now())
                .status(EmployeeTraining.TrainingStatus.ENROLLED)
                .build();

        return employeeTrainingRepository.save(enrollment);
    }

    @Transactional
    public EmployeeTraining updateProgress(Long enrollmentId, int progress) {
        EmployeeTraining enrollment = employeeTrainingRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollment.setProgressPercentage(progress);
        if (progress > 0 && enrollment.getStatus() == EmployeeTraining.TrainingStatus.ENROLLED) {
            enrollment.setStatus(EmployeeTraining.TrainingStatus.IN_PROGRESS);
        }

        return employeeTrainingRepository.save(enrollment);
    }

    @Transactional
    public EmployeeTraining completeTraining(Long enrollmentId, double score, boolean passed, String certificateUrl) {
        EmployeeTraining enrollment = employeeTrainingRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollment.setCompletionDate(LocalDate.now());
        enrollment.setScore(score);
        enrollment.setPassed(passed);
        enrollment.setProgressPercentage(100);
        enrollment.setCertificateUrl(certificateUrl);
        enrollment.setStatus(passed ? EmployeeTraining.TrainingStatus.COMPLETED : EmployeeTraining.TrainingStatus.FAILED);

        Training training = enrollment.getTraining();
        if (training.getType() == Training.TrainingType.CERTIFICATION && passed) {
            enrollment.setCertificateExpiryDate(LocalDate.now().plusYears(1));
        }

        return employeeTrainingRepository.save(enrollment);
    }

    public List<EmployeeTraining> getEmployeeTrainings(Long employeeId) {
        return employeeTrainingRepository.findByEmployeeId(employeeId);
    }

    public List<EmployeeTraining> getCompletedTrainings(Long employeeId) {
        return employeeTrainingRepository.findCompletedByEmployee(employeeId);
    }

    public List<EmployeeTraining> getExpiringCertifications(LocalDate beforeDate) {
        return employeeTrainingRepository.findByCertificateExpiryDateBefore(beforeDate);
    }
}
