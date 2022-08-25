package com.nivorbit.keycloak.storage.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.nivorbit.keycloak.storage.client.UserValidationClient;
import com.nivorbit.keycloak.storage.client.UserValidationResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserValidationClient client;
  @InjectMocks
  private UserService userService;

  @Test
  @DisplayName("Get user with given username")
  void shouldReturnUserWhenUsernameExists() throws IOException {
    when(client.validate(any())).thenReturn(UserValidationResponse.builder().build());
    assertThat(userService.findByUsername(any())).isNotNull();
  }

  @Test
  @DisplayName("Return null when given username not found")
  void shouldReturnNullWhenUsernameDoesNotExist() throws IOException {
    when(client.validate(any())).thenReturn(null);
    assertThat(userService.findByUsername(any())).isNull();
  }
}
