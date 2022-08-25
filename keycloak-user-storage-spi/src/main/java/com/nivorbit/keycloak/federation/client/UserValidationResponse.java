package com.nivorbit.keycloak.federation.client;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserValidationResponse {
  private boolean hasError;
  private String errorMessage;
  private String username;

  @Builder.Default
  private Map<String, String> claims = new HashMap<>();
}
