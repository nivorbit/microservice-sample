package com.nivorbit.keycloak.storage.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserValidationRequest {
  private String username;
  private String password;
  private String sessionId;
  private String customerId;
  private boolean corporate;
}
