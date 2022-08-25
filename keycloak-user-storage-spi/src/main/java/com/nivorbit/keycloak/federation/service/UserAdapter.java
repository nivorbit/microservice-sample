package com.nivorbit.keycloak.federation.service;

import com.nivorbit.keycloak.federation.client.UserValidationResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.EqualsAndHashCode;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.LegacyUserCredentialManager;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.SubjectCredentialManager;
import org.keycloak.models.UserModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapter;

@EqualsAndHashCode(callSuper = true)
public class UserAdapter extends AbstractUserAdapter.Streams {

  private final UserValidationResponse user;

  public UserAdapter(KeycloakSession session, RealmModel realm, ComponentModel model, UserValidationResponse user) {
    super(session, realm, model);
    this.storageId = new StorageId(model.getId(), user.getUsername());
    this.user = user;
  }

  @Override
  public String getUsername() {
    return this.user.getUsername();
  }

  @Override
  public SubjectCredentialManager credentialManager() {
    return new LegacyUserCredentialManager(session, realm, this);
  }

  @Override
  public Map<String, List<String>> getAttributes() {
    MultivaluedHashMap<String, String> attributes = new MultivaluedHashMap<>();
    attributes.add(UserModel.USERNAME, getUsername());
    user.getClaims().forEach((key, value) -> attributes.add(key, value));
    return attributes;
  }

  @Override
  public Stream<String> getAttributeStream(String name) {
    Map<String, List<String>> attributes = getAttributes();
    return (attributes.containsKey(name)) ? attributes.get(name).stream() : Stream.empty();
  }
}
