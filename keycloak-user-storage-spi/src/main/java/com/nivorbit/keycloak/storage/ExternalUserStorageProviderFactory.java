package com.nivorbit.keycloak.storage;

import com.nivorbit.keycloak.storage.provider.ExternalUserStorageProvider;
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
public class ExternalUserStorageProviderFactory
    implements UserStorageProviderFactory<ExternalUserStorageProvider> {

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
  public ExternalUserStorageProvider create(
      KeycloakSession keycloakSession, ComponentModel componentModel) {
    return new ExternalUserStorageProvider(keycloakSession, componentModel);
  }

  @Override
  public String getId() {
    return "external-user-provider";
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
