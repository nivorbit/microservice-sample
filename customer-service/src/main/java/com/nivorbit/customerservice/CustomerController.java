package com.nivorbit.customerservice;

import java.util.List;
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

@Slf4j
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

  private final CustomerService customerService;

  @PreAuthorize("hasRole('USER')")
  @GetMapping("/{id}")
  public Customer findById(@PathVariable UUID id) {
    log.info("fetch customer {}", id);
    return customerService.findById(id).orElse(null);
  }

  @PreAuthorize("hasRole('USER')")
  @GetMapping
  public List<Customer> findAll() {
    log.info("fetch all customers");
    return customerService.findAll();
  }

  @PreAuthorize("hasRole('USER')")
  @PostMapping
  public void findById(@RequestBody Customer customer) {
    log.info("new customer registration {}", customer);
    customerService.add(customer);
  }

  @PreAuthorize("hasRole('USER')")
  @DeleteMapping("/{id}")
  public void delete(@PathVariable UUID id) {
    log.info("delete customer {}", id);
    customerService.delete(id);
  }

}
