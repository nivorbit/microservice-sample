package com.nivorbit.customerservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepository {

  private final List<Customer> customers = new ArrayList<>();

  public Optional<Customer> findById(UUID id) {
    return customers.stream().filter(customer -> customer.getId().equals(id)).findFirst();
  }

  public List<Customer> findAll() {
    return customers;
  }

  public void delete(UUID id) {
    customers.removeIf(customer -> customer.getId().equals(id));
  }

  public void add(Customer customer) {
    customers.add(customer);
  }
}
