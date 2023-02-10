package com.bojan.lora.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bojan.lora.domain.entity.Measurement;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
}
