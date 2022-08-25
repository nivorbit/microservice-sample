package com.nivorbit.keycloak.federation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nivorbit.keycloak.federation.provider.ExternalUserFederationProvider;
import org.junit.jupiter.api.Test;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;

class ExternalUserFederationProviderFactoryTest {

  @Test
  void testCreate() {
    ExternalUserFederationProviderFactory factory =
        new ExternalUserFederationProviderFactory();
    assertThat(factory).isNotNull();

    var componentModel = mock(ComponentModel.class);
    componentModel = mock(ComponentModel.class);


    when(componentModel.get("url")).thenReturn("http://localhost:6666");

    ExternalUserFederationProvider externalUserFederationProvider =
        factory.create(mock(KeycloakSession.class), componentModel);
    assertThat(externalUserFederationProvider).isNotNull();
  }

  @Test
  void testProviderId() {
    ExternalUserFederationProviderFactory factory =
        new ExternalUserFederationProviderFactory();
    assertThat(factory).isNotNull();
    assertThat(factory.getId()).isEqualTo("external-user-provider");
  }
}
