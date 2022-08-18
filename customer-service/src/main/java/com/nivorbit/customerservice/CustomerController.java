package com.nivorbit.customerservice;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

  private final CustomerService customerService;

  @PreAuthorize("hasRole('USER')")
  @GetMapping("/{id}")
  public Mono<Customer> findById(@PathVariable UUID id) {
    log.info("fetch customer {}", id);
    return customerService.findById(id);
  }

  @PreAuthorize("hasRole('USER')")
  @GetMapping
  public Flux<Customer> findAll() {
    log.info("fetch all customers");
    return customerService.findAll();
  }

  @PreAuthorize("hasRole('USER')")
  @PostMapping
  public Mono<Void> create(@RequestBody Customer customer) {
    log.info("new customer registration {}", customer);
    customerService.add(customer);
    return Mono.empty();
  }

  @PreAuthorize("hasRole('USER')")
  @DeleteMapping("/{id}")
  public Mono<Void> delete(@PathVariable UUID id) {
    log.info("delete customer {}", id);
    customerService.delete(id);
    return Mono.empty();
  }

}
