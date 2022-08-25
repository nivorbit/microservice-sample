package com.nivorbit.keycloak.storage.client;

import java.io.IOException;
import javax.ws.rs.WebApplicationException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.component.ComponentModel;
import org.keycloak.connections.httpclient.HttpClientProvider;
import org.keycloak.models.KeycloakSession;

public class UserValidationClient {

  private final CloseableHttpClient httpClient;
  private final String baseUrl;

  public UserValidationClient(KeycloakSession session, ComponentModel model) {
    this.httpClient = session.getProvider(HttpClientProvider.class).getHttpClient();
    this.baseUrl = model.get("url");
  }

  public UserValidationResponse validate(UserValidationRequest request) throws IOException {
    SimpleHttp.Response response = SimpleHttp.doPost(this.baseUrl, httpClient).json(request).asResponse();
    if (response.getStatus() > 200) {
      throw new WebApplicationException(response.getStatus());
    }
    return response.asJson(UserValidationResponse.class);
  }
}
