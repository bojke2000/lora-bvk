package com.bojan.lora.service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bojan.lora.domain.LoraMtsRepository;
import com.bojan.lora.domain.entity.LoraMts;

@Service
public class LoraMtsService {

  @Autowired
  private LoraMtsRepository loraMtsRepository;

  public List<LoraMts> getAllLoraMts() {
    return loraMtsRepository.findAll();
  }

  public LoraMts getLoraMtsById(Long id) {
    Optional<LoraMts> loraMts = loraMtsRepository.findById(id);
    return loraMts.orElse(null);
  }

  public LoraMts createLoraMts(LoraMts loraMts) {
    return loraMtsRepository.save(loraMts);
  }

  public LoraMts updateLoraMts(Long id, LoraMts loraMts) {
    loraMts.setId(id);
    return loraMtsRepository.save(loraMts);
  }

  public void deleteLoraMts(Long id) {
    loraMtsRepository.deleteById(id);
  }
}
