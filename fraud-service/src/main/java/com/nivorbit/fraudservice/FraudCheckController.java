package com.nivorbit.fraudservice;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/fraud-check")
@RequiredArgsConstructor
public class FraudCheckController {

  private final FraudService fraudService;

  @GetMapping("/{customerId}")
  public FraudCheckResponse isFraudster(@PathVariable UUID customerId) {
    log.info("fraud check request for customer {}", customerId);
    return new FraudCheckResponse(fraudService.isFraudulentCustomer(customerId));
  }
}
