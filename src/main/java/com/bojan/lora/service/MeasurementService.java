package com.bojan.lora.service;

import org.springframework.stereotype.Service;

import com.bojan.lora.domain.entity.Measurement;
import com.bojan.lora.domain.repository.MeasurementRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MeasurementService {

  private final MeasurementRepository measurementRepository;

  public MeasurementService(MeasurementRepository measurementRepository) {
    this.measurementRepository = measurementRepository;
  }

  public Measurement createMeasurement(Measurement measurement) {
    return measurementRepository.save(measurement);
  }

  public Optional<Measurement> findMeasurementById(Long id) {
    return measurementRepository.findById(id);
  }

  public List<Measurement> findAllMeasurements() {
    return measurementRepository.findAll();
  }

  public Measurement updateMeasurement(Measurement measurement) {
    return measurementRepository.save(measurement);
  }

  public void deleteMeasurement(Measurement measurement) {
    measurementRepository.delete(measurement);
  }
}
