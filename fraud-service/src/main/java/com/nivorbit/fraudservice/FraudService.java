package com.nivorbit.fraudservice;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FraudService {

  private final FraudCheckHistoryRepository fraudCheckHistoryRepository;

  public boolean isFraudulentCustomer(UUID customerId) {
    fraudCheckHistoryRepository.save(FraudCheckHistory.builder()
            .id(UUID.randomUUID())
            .customerId(customerId)
            .createdAt(LocalDateTime.now())
            .fraudster(false)
        .build());
    return false;
  }
}
