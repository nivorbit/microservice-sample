package com.nivorbit.customerservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class CustomerRepository {

  private final List<Customer> customers = new ArrayList<>();

  public Mono<Customer> findById(UUID id) {
    return Mono.justOrEmpty(customers.stream().filter(customer -> customer.getId().equals(id)).findFirst());
  }

  public Flux<Customer> findAll() {
    return Flux.fromIterable(customers);
  }

  public void delete(UUID id) {
    customers.removeIf(customer -> customer.getId().equals(id));
  }

  public void add(Customer customer) {
    customers.add(customer);
  }
}
