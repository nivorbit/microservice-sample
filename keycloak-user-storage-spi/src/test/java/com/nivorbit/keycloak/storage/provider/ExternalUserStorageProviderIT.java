package com.nivorbit.keycloak.storage.provider;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.restassured.response.Response;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@Slf4j
@Testcontainers
class ExternalUserStorageProviderIT {

  private static final String REALM = "inmemory";

  @Container
  private static final KeycloakContainer keycloak =
      new KeycloakContainer()
          .withRealmImportFile("/realm.json")
          .withProviderClassesFrom("target/classes");

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
  @ValueSource(strings = {"user", "admin", "test"})
  void testLoginAsUserAndCheckAccessToken(String username) throws IOException {
    String accessTokenString =
        requestToken(username, "test1234").then().statusCode(200).extract().path("access_token");

    ObjectMapper mapper = new ObjectMapper();
    TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {};

    byte[] tokenPayload = Base64.getDecoder().decode(accessTokenString.split("\\.")[1]);
    Map<String, Object> payload = mapper.readValue(tokenPayload, typeRef);

    assertThat(payload.get("preferred_username"), is(username));
  }

  @Test
  void testLoginAsUserWithInvalidPassword() {
    requestToken("test", "1234").then().statusCode(401);
  }

  @Test
  void testAccessingUsersAsAdmin() {
    Keycloak kcAdmin = keycloak.getKeycloakAdminClient();
    UsersResource usersResource = kcAdmin.realm(REALM).users();
    List<UserRepresentation> users = usersResource.search("user");
    assertThat(users, is(not(empty())));

    String userId = users.get(0).getId();
    UserResource userResource = usersResource.get(userId);
    assertThat(userResource.toRepresentation().getUsername(), is("user"));
  }

  private Response requestToken(String username, String password) {
    String tokenEndpoint =
        given()
            .when()
            .get(
                keycloak.getAuthServerUrl()
                    + "realms/"
                    + REALM
                    + "/.well-known/openid-configuration")
            .then()
            .statusCode(200)
            .extract()
            .path("token_endpoint");
    return given()
        .contentType("application/x-www-form-urlencoded")
        .formParam("username", username)
        .formParam("password", password)
        .formParam("grant_type", "password")
        .formParam("client_id", KeycloakContainer.ADMIN_CLI_CLIENT)
        .formParam("scope", "openid")
        .when()
        .post(tokenEndpoint);
  }
}
