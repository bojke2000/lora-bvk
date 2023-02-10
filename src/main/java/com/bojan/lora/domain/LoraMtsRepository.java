package com.bojan.lora.domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bojan.lora.domain.entity.LoraMts;

@Repository
public interface LoraMtsRepository extends JpaRepository<LoraMts, Long> {

}
