package com.example.alfa.repositories;

import com.example.alfa.models.Benefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BenefitRepository extends JpaRepository<Benefit, Long> {
    List<Benefit> findByIsActiveTrue();
    List<Benefit> findByType(Benefit.BenefitType type);
}
