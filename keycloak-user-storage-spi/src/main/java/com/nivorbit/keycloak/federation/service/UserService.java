package com.nivorbit.keycloak.federation.service;

import com.nivorbit.keycloak.federation.client.UserValidationClient;
import com.nivorbit.keycloak.federation.client.UserValidationRequest;
import com.nivorbit.keycloak.federation.client.UserValidationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UserService {
  private final UserValidationClient userValidationClient;

  public UserValidationResponse findByUsername(UserValidationRequest request) {
    try {
      UserValidationResponse userValidationResponse = userValidationClient.validate(request);
      userValidationResponse.setUsername(request.getUsername());
      return userValidationResponse;
    } catch (Exception exception) {
      log.error("An error occurred while validating user: {}", exception.getMessage());
    }
    return null;
  }
}
