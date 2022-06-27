package com.nivorbit.fraudservice;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FraudCheckResponse {
  private boolean fraudster;
}
