package com.bojan.lora.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bojan.lora.domain.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    public Optional<Customer> findByDevEui(String devEui);
}

