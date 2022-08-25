package com.nivorbit.keycloak.federation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.nivorbit.keycloak.federation.client.UserValidationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;

class UserAdapterTest {

  @Test
  @DisplayName("Create UserAdapter from User")
  void shouldCreateUserAdapterWhenUserIsGiven() {
    UserAdapter userAdapter =
        new UserAdapter(
            mock(KeycloakSession.class),
            mock(RealmModel.class),
            mock(ComponentModel.class),
            UserValidationResponse.builder().username("username").build());

    assertThat(userAdapter).isNotNull();
    assertThat(userAdapter.getUsername()).isEqualTo("username");
  }
}
