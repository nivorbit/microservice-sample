package com.nivorbit.keycloak.storage.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PasswordVerifyTest {

  private final PasswordVerify passwordVerify = new PasswordVerify();

  @Test
  @DisplayName("Verify password both inputs are same")
  void should_verify_when_does_not_have_error() {
    assertThat(passwordVerify.verify("test")).isTrue();
  }

 /* @Test
  @DisplayName("Not verify password both inputs are different")
  void should_not_verify_when_has_error() {
    assertThat(passwordVerify.verify("test")).isFalse();
  }*/
}
