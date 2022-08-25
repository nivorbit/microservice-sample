package com.nivorbit.keycloak.storage.service;

import com.nivorbit.keycloak.storage.client.UserValidationClient;
import com.nivorbit.keycloak.storage.client.UserValidationRequest;
import com.nivorbit.keycloak.storage.client.UserValidationResponse;
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
