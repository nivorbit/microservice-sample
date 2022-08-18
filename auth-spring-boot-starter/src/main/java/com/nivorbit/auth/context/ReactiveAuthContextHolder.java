package com.nivorbit.auth.context;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

public final class ReactiveAuthContextHolder {
  private static final ObjectMapper objectMapper = new ObjectMapper().configure(
      DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).findAndRegisterModules();

  public static Mono<AuthContext> getContext() {
    return jwt().map(jwt -> objectMapper.convertValue(jwt.getClaims(), AuthContext.class));
  }

  private static Mono<Jwt> jwt() {
    return ReactiveSecurityContextHolder.getContext()
        .map(context -> context.getAuthentication().getPrincipal())
        .cast(Jwt.class);
  }
}
