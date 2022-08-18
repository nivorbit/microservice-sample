package com.nivorbit.auth.context;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class AuthContextHolder {
  private static final ObjectMapper objectMapper = new ObjectMapper().configure(
      DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).findAndRegisterModules();

  public static AuthContext getContext() {
    return objectMapper.convertValue(jwt().getClaims(), AuthContext.class);
  }

  private static Jwt jwt() {
    return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}
