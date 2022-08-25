package com.nivorbit.keycloak.federation.provider;

import com.nivorbit.keycloak.federation.client.UserValidationClient;
import com.nivorbit.keycloak.federation.client.UserValidationRequest;
import com.nivorbit.keycloak.federation.client.UserValidationResponse;
import com.nivorbit.keycloak.federation.service.PasswordVerify;
import com.nivorbit.keycloak.federation.service.UserAdapter;
import com.nivorbit.keycloak.federation.service.UserService;
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
public class ExternalUserFederationProvider
    implements UserStorageProvider, UserLookupProvider, CredentialInputValidator {

  private final KeycloakSession session;
  private final ComponentModel model;
  private final UserService userService;
  private final PasswordVerify passwordVerify;

  public ExternalUserFederationProvider(KeycloakSession session, ComponentModel model) {
    this(session, model, new UserService(new UserValidationClient(session, model)),
        new PasswordVerify());
  }

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
  public boolean isValid(RealmModel realmModel, UserModel userModel,
                         CredentialInput credentialInput) {
    if (!supportsCredentialType(credentialInput.getType())
        || !(credentialInput instanceof UserCredentialModel)) {
      return false;
    }

    passwordVerify.verify(credentialInput.getChallengeResponse());
    return true;
  }


  private UserAdapter findByUsername(String username, RealmModel realmModel) {
    UserValidationResponse userValidationResponse =
        userService.findByUsername(createUserValidationRequest(username));
    if (!Objects.isNull(userValidationResponse) && !userValidationResponse.isHasError()) {
      return new UserAdapter(
          session, realmModel, model, userValidationResponse);
    }

    return null;
  }

  private UserValidationRequest createUserValidationRequest(String username) {
    var customerNumber =
        this.session.getContext().getRequestHeaders().getHeaderString("X-CUSTOMER-NUMBER");
    var corporate = this.session.getContext().getRequestHeaders().getHeaderString("X-CORPORATE");
    var sessionId = this.session.getContext().getRequestHeaders().getHeaderString("X-SESSION-ID");
    var password = this.session.getContext().getRequestHeaders().getHeaderString("X-PASSWORD");

    return UserValidationRequest.builder()
        .customerId(customerNumber)
        .username(username)
        .password(password)
        .sessionId(sessionId)
        .corporate(Boolean.parseBoolean(corporate))
        .build();
  }
}
