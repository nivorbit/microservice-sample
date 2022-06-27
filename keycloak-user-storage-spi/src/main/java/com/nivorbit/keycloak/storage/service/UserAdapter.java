package com.nivorbit.keycloak.storage.service;

import com.nivorbit.keycloak.storage.model.User;
import lombok.EqualsAndHashCode;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapter;

@EqualsAndHashCode(callSuper = true)
public class UserAdapter extends AbstractUserAdapter.Streams {

  private final User user;

  public UserAdapter(
      KeycloakSession session, RealmModel realm, ComponentModel componentModel, User user) {
    super(session, realm, componentModel);
    this.storageId = new StorageId(componentModel.getId(), user.getUsername());
    this.user = user;
  }

  @Override
  public String getUsername() {
    return this.user.getUsername();
  }
}
