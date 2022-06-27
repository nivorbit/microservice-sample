package com.nivorbit.apigateway.security.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nivorbit.apigateway.security.BearerTokenResolver;
import com.nivorbit.apigateway.security.TokenStore;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Profile({"keycloak", "dev"})
@RequiredArgsConstructor
public class TokenExchangeFilter implements GlobalFilter {

  @Value("${client.id}")
  private final String clientId;
  @Value("${client.secret}")
  private final String clientSecret;
  @Value("${client.issuer-uri}")
  private final String issueUrl;
  @Value("${client.audience}")
  private final String audience;
  private final WebClient webClient;

  private final BearerTokenResolver bearerTokenResolver;

  private final TokenStore tokenStore;

  private static final String TOKEN_EXCHANGE_GRANT_TYPE = "urn:ietf:params:oauth:grant-type:token-exchange";
  private static final String TOKEN_TYPE = "urn:ietf:params:oauth:token-type:refresh_token";

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String bearerToken = bearerTokenResolver.resolve(exchange);

    if(StringUtils.hasText(tokenStore.getToken(bearerToken))) {
      log.info("Exchange token found in store for bearer: {}", bearerToken);
      exchange.getRequest().mutate().header("Authorization", "Bearer " + tokenStore.getToken(bearerToken));
      return chain.filter(exchange);
    }

    MultiValueMap<String, String> bodyValues = new LinkedMultiValueMap<>();
    bodyValues.add("client_id", clientId);
    bodyValues.add("client_secret", clientSecret);
    bodyValues.add("grant_type", TOKEN_EXCHANGE_GRANT_TYPE);
    bodyValues.add("requested_token_type", TOKEN_TYPE);
    bodyValues.add("subject_token", bearerToken);
    bodyValues.add("audience", audience);

    log.info("Exchanging token for bearer: {}", bearerToken);

    return webClient.post()
        .uri( issueUrl + "/protocol/openid-connect/token")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromFormData(bodyValues))
        .retrieve()
        .bodyToMono(TokenExchangeResponse.class)
        .map(it -> {
          tokenStore.store(bearerToken, it.accessToken);
          return exchange.getRequest().mutate().header("Authorization", "Bearer " + it.accessToken);
        })
        .flatMap(ignored -> chain.filter(exchange));
  }

  @Data
  static class TokenExchangeResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;
  }

}
