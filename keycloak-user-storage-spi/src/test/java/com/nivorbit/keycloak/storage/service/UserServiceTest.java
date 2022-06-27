package com.nivorbit.keycloak.storage.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserServiceTest {

  private final UserService userService = new UserService();

  @Test
  @DisplayName("Get user with given username")
  void shouldReturnUserWhenUsernameExists() {
    assertThat(userService.findByUsername("user")).isNotNull();
  }

  @Test
  @DisplayName("Return null when given username not found")
  void shouldReturnNullWhenUsernameDoesNotExist() {
    assertThat(userService.findByUsername("joe")).isNull();
  }
}
