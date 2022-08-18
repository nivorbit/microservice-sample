package com.nivorbit.auth.servlet.configuration;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Import({SecurityConfiguration.class})
public class ServletAuthConfiguration {

  @Bean
  @ConditionalOnMissingBean(JwtAuthenticationConverter.class)
  JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new RealmRoleConverter());
    return jwtAuthenticationConverter;
  }

  static class RealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
      Map<String, Object> realms = jwt.getClaimAsMap("realm_access");

      if (Objects.isNull(realms)) return Set.of();

      Object roleClaims = realms.get("roles");

      if (!(roleClaims instanceof List) || Objects.isNull(roleClaims)) return Set.of();

      List<String> roles = (List<String>) roleClaims;
      return roles.stream()
          .map(role -> "ROLE_" + role)
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toList());
    }
  }
}
