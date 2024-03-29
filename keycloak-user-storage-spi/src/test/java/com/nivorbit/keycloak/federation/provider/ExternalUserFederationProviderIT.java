package com.nivorbit.keycloak.federation.provider;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Disabled
@Slf4j
@Testcontainers
class ExternalUserFederationProviderIT {

  private static final String REALM = "test";

  private static Network network = Network.newNetwork();
  @Container
  private static final KeycloakContainer keycloak =
      new KeycloakContainer("quay.io/keycloak/keycloak:19.0.1")
          .withRealmImportFile("/test-realm.json")
          .withProviderClassesFrom("target/classes");

  @Container
  private static final GenericContainer<?> apiMock = new GenericContainer<>("muonsoft/openapi-mock:latest")
      .withExposedPorts(8080)
      .withClasspathResourceMapping("api.yaml", "/tmp/spec.yaml", BindMode.READ_ONLY)
      .withEnv(Map.of(
          "OPENAPI_MOCK_SPECIFICATION_URL", "/tmp/spec.yaml",
          "OPENAPI_MOCK_USE_EXAMPLES", "if_present"
      ))
      .withLogConsumer(new Slf4jLogConsumer(log))
      .withNetwork(network)
      .withNetworkAliases("api");


  @ParameterizedTest
  @ValueSource(strings = {KeycloakContainer.MASTER_REALM, REALM})
  void testRealms(String realm) {
    String accountServiceUrl =
        given()
            .when()
            .get(keycloak.getAuthServerUrl() + "realms/" + realm)
            .then()
            .statusCode(200)
            .body("realm", equalTo(realm))
            .extract()
            .path("account-service");

    given().when().get(accountServiceUrl).then().statusCode(200);
  }

  @ParameterizedTest
  @CsvSource({"test,john,john", "test,admin,admin", "test,user,user"})
  void testLoginAsUserAndCheckAccessToken(String realm, String username, String password) {
    var authServerUrl = keycloak.getAuthServerUrl();
    String accessToken = getAccessToken(realm, username, password);

    given().auth().oauth2(accessToken)
        .when().get(authServerUrl + "realms/" + realm + "/protocol/openid-connect/userinfo")
        .then()
        .statusCode(200)
        .body(allOf(
            containsString("username"),
            containsString(username)
        ));
  }

  private String getAccessToken(String realm, String username, String password) {
    var authServerUrl = keycloak.getAuthServerUrl();
    return given()
        .contentType("application/x-www-form-urlencoded")
        .headers(Map.of(
            "X-SESSION-ID", "1223",
            "X-CUSTOMER-NUMBER", "1234",
            "X-CORPORATE","true"
        ))
        .formParams(Map.of(
            "username", username,
            "password", password,
            "grant_type", "password",
            "client_id", "test-service",
            "client_secret", "secret"
        ))
        .post(authServerUrl + "realms/" + realm + "/protocol/openid-connect/token")
        .then().assertThat().statusCode(200)
        .extract().path("access_token");
  }
}
