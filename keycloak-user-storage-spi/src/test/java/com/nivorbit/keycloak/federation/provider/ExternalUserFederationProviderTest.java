package com.nivorbit.keycloak.federation.provider;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nivorbit.keycloak.federation.client.UserValidationResponse;
import com.nivorbit.keycloak.federation.service.PasswordVerify;
import com.nivorbit.keycloak.federation.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExternalUserFederationProviderTest {

  @Mock
  private KeycloakSession keycloakSession;
  @Mock
  private RealmModel realmModel;
  @Mock
  private ComponentModel componentModel;
  @Mock
  private PasswordVerify passwordVerify;
  @Mock
  private UserService userService;

  @InjectMocks
  private ExternalUserFederationProvider externalUserFederationProvider;

  @Test
  void shouldSupportPasswordType() {
    assertThat(externalUserFederationProvider.supportsCredentialType("password")).isTrue();
  }

  @Test
  void shouldNotSupportType() {
    assertThat(externalUserFederationProvider.supportsCredentialType("password1")).isFalse();
  }

  @Test
  void shouldConfigureForPasswordType() {
    assertThat(externalUserFederationProvider.isConfiguredFor(mock(RealmModel.class), mock(UserModel.class), "password")).isTrue();
  }

  @Test
  void shouldNotConfigureExceptForPasswordType() {
    assertThat(externalUserFederationProvider.isConfiguredFor(mock(RealmModel.class), mock(UserModel.class), "password1")).isFalse();
  }

  @Test
  void shouldGetByUserId() {
    when(userService.findByUsername(any())).thenReturn(UserValidationResponse.builder().username("username").build());
    UserModel userModel = externalUserFederationProvider.getUserById( "username", mock(RealmModel.class));
    assertThat(userModel).isNotNull();
    assertThat(userModel.getUsername()).isEqualTo("username");
  }

  @Test
  void shouldGetByUsername() {
    when(userService.findByUsername(any())).thenReturn(UserValidationResponse.builder().username("username").build());
    UserModel userModel = externalUserFederationProvider.getUserByUsername( "username", mock(RealmModel.class));
    assertThat(userModel).isNotNull();
    assertThat(userModel.getUsername()).isEqualTo("username");
  }

  @Test
  void shouldGetByEmail() {
    when(userService.findByUsername(any())).thenReturn(UserValidationResponse.builder().username("username").build());
    UserModel userModel = externalUserFederationProvider.getUserByEmail( "username", mock(RealmModel.class));
    assertThat(userModel).isNotNull();
    assertThat(userModel.getUsername()).isEqualTo("username");
  }
}