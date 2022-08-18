package com.nivorbit.customerservice;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final RestTemplate restTemplate;

  public Mono<Customer> findById(UUID id) {
    return ReactiveRequestContextHolder.getContext().flatMap(requestContext -> {
      log.info("Customer Number: {}", requestContext.getCustomerNumber());
      log.info("Account No: {}", requestContext.getAccountNo());
      return customerRepository.findById(id);
    });

  }

  public Flux<Customer> findAll() {
    return customerRepository.findAll();
  }

  public void add(Customer customer) {
   /* FraudCheckResponse fraudCheckResponse = restTemplate
            .getForObject("http://fraud-service:8080/fraud-check/{customerId}", FraudCheckResponse.class, customer.getId());

    if (fraudCheckResponse != null && fraudCheckResponse.isFraudster()) {
      throw new IllegalStateException("fraudster");
    }*/
    customerRepository.add(customer);
  }

  public void delete(UUID id) {
    customerRepository.delete(id);
  }
}
