package com.nivorbit.customerservice;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final RestTemplate restTemplate;

  public Optional<Customer> findById(UUID id) {
    return customerRepository.findById(id);
  }

  public List<Customer> findAll() {
    return customerRepository.findAll();
  }

  public void add(Customer customer) {
    FraudCheckResponse fraudCheckResponse = restTemplate
            .getForObject("http://fraud-service:8080/fraud-check/{customerId}", FraudCheckResponse.class, customer.getId());

    if (fraudCheckResponse != null && fraudCheckResponse.isFraudster()) {
      throw new IllegalStateException("fraudster");
    }

    customerRepository.add(customer);
  }

  public void delete(UUID id) {
    customerRepository.delete(id);
  }
}
