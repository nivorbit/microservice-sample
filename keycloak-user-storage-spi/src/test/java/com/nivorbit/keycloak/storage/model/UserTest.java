package com.nivorbit.keycloak.storage.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

  @DisplayName("User object create")
  @Test
  void shouldCreateUserWithNameAndPassword() {
    User user = new User("user", "password");
    assertThat(user).isNotNull();
    assertThat(user.getUsername()).isEqualTo("user");
    assertThat(user.getPassword()).isEqualTo("password");
  }
}
