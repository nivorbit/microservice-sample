package com.nivorbit.keycloak.federation;

import com.nivorbit.keycloak.federation.provider.ExternalUserFederationProvider;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.component.ComponentModel;
import org.keycloak.component.ComponentValidationException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;
import org.keycloak.utils.StringUtil;

@Slf4j
public class ExternalUserFederationProviderFactory
    implements UserStorageProviderFactory<ExternalUserFederationProvider> {

  private static final List<ProviderConfigProperty> configMetadata;

  static {
    configMetadata = ProviderConfigurationBuilder.create()
        .property().name("url")
        .type(ProviderConfigProperty.STRING_TYPE)
        .label("URL")
        .defaultValue("")
        .helpText("Url path to api")
        .add().build();
  }

  @Override
  public ExternalUserFederationProvider create(
      KeycloakSession keycloakSession, ComponentModel componentModel) {
    return new ExternalUserFederationProvider(keycloakSession, componentModel);
  }

  @Override
  public String getId() {
    return "external-user-federation";
  }

  @Override
  public List<ProviderConfigProperty> getConfigProperties() {
    return configMetadata;
  }

  @Override
  public void validateConfiguration(KeycloakSession session, RealmModel realm,
                                    ComponentModel config) throws ComponentValidationException {
    if (StringUtil.isBlank(config.get("url"))) {
      throw new ComponentValidationException("Please provide url");
    }
  }
}
