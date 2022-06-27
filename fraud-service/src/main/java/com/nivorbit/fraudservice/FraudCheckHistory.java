package com.nivorbit.fraudservice;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FraudCheckHistory {
  private UUID id;
  private UUID customerId;
  private boolean fraudster;
  private LocalDateTime createdAt;
}
