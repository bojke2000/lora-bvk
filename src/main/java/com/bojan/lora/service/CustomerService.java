package com.bojan.lora.service;

import org.springframework.stereotype.Service;

import com.bojan.lora.domain.entity.Customer;
import com.bojan.lora.domain.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

  private final CustomerRepository customerRepository;

  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  public Customer createCustomer(Customer customer) {
    return customerRepository.save(customer);
  }

  public Optional<Customer> findCustomerById(Long id) {
    return customerRepository.findById(id);
  }

  public List<Customer> findAllCustomers() {
    return customerRepository.findAll();
  }

  public Customer updateCustomer(Customer customer) {
    return customerRepository.save(customer);
  }

  public void deleteCustomer(Customer customer) {
    customerRepository.delete(customer);
  }
}
