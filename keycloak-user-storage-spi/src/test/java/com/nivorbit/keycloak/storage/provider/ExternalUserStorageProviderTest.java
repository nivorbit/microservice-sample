package com.nivorbit.keycloak.storage.provider;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nivorbit.keycloak.storage.client.UserValidationResponse;
import com.nivorbit.keycloak.storage.service.PasswordVerify;
import com.nivorbit.keycloak.storage.service.UserService;
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
class ExternalUserStorageProviderTest {

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
  private ExternalUserStorageProvider externalUserStorageProvider;

  @Test
  void shouldSupportPasswordType() {
    assertThat(externalUserStorageProvider.supportsCredentialType("password")).isTrue();
  }

  @Test
  void shouldNotSupportType() {
    assertThat(externalUserStorageProvider.supportsCredentialType("password1")).isFalse();
  }

  @Test
  void shouldConfigureForPasswordType() {
    assertThat(externalUserStorageProvider.isConfiguredFor(mock(RealmModel.class), mock(UserModel.class), "password")).isTrue();
  }

  @Test
  void shouldNotConfigureExceptForPasswordType() {
    assertThat(externalUserStorageProvider.isConfiguredFor(mock(RealmModel.class), mock(UserModel.class), "password1")).isFalse();
  }

  @Test
  void shouldGetByUserId() {
    when(userService.findByUsername(any())).thenReturn(UserValidationResponse.builder().username("username").build());
    UserModel userModel = externalUserStorageProvider.getUserById( "username", mock(RealmModel.class));
    assertThat(userModel).isNotNull();
    assertThat(userModel.getUsername()).isEqualTo("username");
  }

  @Test
  void shouldGetByUsername() {
    when(userService.findByUsername(any())).thenReturn(UserValidationResponse.builder().username("username").build());
    UserModel userModel = externalUserStorageProvider.getUserByUsername( "username", mock(RealmModel.class));
    assertThat(userModel).isNotNull();
    assertThat(userModel.getUsername()).isEqualTo("username");
  }

  @Test
  void shouldGetByEmail() {
    when(userService.findByUsername(any())).thenReturn(UserValidationResponse.builder().username("username").build());
    UserModel userModel = externalUserStorageProvider.getUserByEmail( "username", mock(RealmModel.class));
    assertThat(userModel).isNotNull();
    assertThat(userModel.getUsername()).isEqualTo("username");
  }
}