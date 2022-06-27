package com.nivorbit.keycloak.storage;

import com.nivorbit.keycloak.storage.provider.ExternalUserStorageProvider;
import com.nivorbit.keycloak.storage.service.PasswordVerify;
import com.nivorbit.keycloak.storage.service.UserService;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

public class ExternalUserStorageProviderFactory
    implements UserStorageProviderFactory<ExternalUserStorageProvider> {

  public static final String PROVIDER_NAME = "external-user-storage";

  private final UserService userService = new UserService();
  private final PasswordVerify passwordVerify = new PasswordVerify();

  @Override
  public ExternalUserStorageProvider create(
      KeycloakSession keycloakSession, ComponentModel componentModel) {
    return new ExternalUserStorageProvider(
        keycloakSession, componentModel, this.userService, this.passwordVerify);
  }

  @Override
  public String getId() {
    return PROVIDER_NAME;
  }
}
