package com.nivorbit.keycloak.storage.provider;

import com.nivorbit.keycloak.storage.model.User;
import com.nivorbit.keycloak.storage.service.PasswordVerify;
import com.nivorbit.keycloak.storage.service.UserAdapter;
import com.nivorbit.keycloak.storage.service.UserService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;

@Slf4j
@RequiredArgsConstructor
public class ExternalUserStorageProvider
    implements UserStorageProvider, UserLookupProvider, CredentialInputValidator {

  private final KeycloakSession keycloakSession;
  private final ComponentModel componentModel;
  private final UserService userService;
  private final PasswordVerify passwordVerify;

  @Override
  public void close() {
    /* There is no action */
  }

  @Override
  public UserModel getUserById(String keycloakUserId, RealmModel realmModel) {
    log.debug("getUserById: {}", keycloakUserId);
    return findByUsername(StorageId.externalId(keycloakUserId), realmModel);
  }

  @Override
  public UserModel getUserByUsername(String username, RealmModel realmModel) {
    log.debug("getUserByUsername: {}", username);
    return findByUsername(username, realmModel);
  }

  @Override
  public UserModel getUserByEmail(String email, RealmModel realmModel) {
    log.debug("getUserByEmail: {}", email);
    return findByUsername(email, realmModel);
  }

  @Override
  public boolean supportsCredentialType(String credentialType) {
    return PasswordCredentialModel.TYPE.equals(credentialType);
  }

  @Override
  public boolean isConfiguredFor(
      RealmModel realmModel, UserModel userModel, String credentialType) {
    return supportsCredentialType(credentialType);
  }

  @Override
  public boolean isValid(
      RealmModel realmModel, UserModel userModel, CredentialInput credentialInput) {
    if (!supportsCredentialType(credentialInput.getType())
        || !(credentialInput instanceof UserCredentialModel)) {
      return false;
    }

    User user = this.userService.findByUsername(StorageId.externalId(userModel.getId()));
    if (Objects.isNull(user)) return false;

    return passwordVerify.verify(credentialInput.getChallengeResponse(), user.getPassword());
  }

  private UserAdapter findByUsername(String username, RealmModel realmModel) {
    User user = userService.findByUsername(username);
    if (!Objects.isNull(user)) {
      return new UserAdapter(
          keycloakSession, realmModel, componentModel, user);
    }

    return null;
  }
}
