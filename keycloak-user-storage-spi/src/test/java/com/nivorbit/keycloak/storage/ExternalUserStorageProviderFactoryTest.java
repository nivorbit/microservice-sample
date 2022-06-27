package com.nivorbit.keycloak.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.nivorbit.keycloak.storage.provider.ExternalUserStorageProvider;
import org.junit.jupiter.api.Test;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;

class ExternalUserStorageProviderFactoryTest {

  @Test
  void testCreate() {
    ExternalUserStorageProviderFactory factory =
        new ExternalUserStorageProviderFactory();
    assertThat(factory).isNotNull();

    ExternalUserStorageProvider externalUserStorageProvider =
        factory.create(mock(KeycloakSession.class), mock(ComponentModel.class));
    assertThat(externalUserStorageProvider).isNotNull();
  }

  @Test
  void testProviderId() {
    ExternalUserStorageProviderFactory factory =
        new ExternalUserStorageProviderFactory();
    assertThat(factory).isNotNull();
    assertThat(factory.getId()).isEqualTo(ExternalUserStorageProviderFactory.PROVIDER_NAME);
  }
}
