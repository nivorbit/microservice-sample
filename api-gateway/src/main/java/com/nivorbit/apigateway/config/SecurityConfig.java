package com.nivorbit.apigateway.config;

import com.nivorbit.apigateway.security.TokenStore;
import com.nivorbit.apigateway.security.store.InMemoryTokenStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  @Bean
  SecurityWebFilterChain filterChain(ServerHttpSecurity httpSecurity) {
    httpSecurity.authorizeExchange(exchange ->
            exchange.pathMatchers("/actuator/**").permitAll().anyExchange().authenticated()
        )
        .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);

    httpSecurity.csrf().disable();
    return httpSecurity.build();
  }

  @Bean
  TokenStore tokenStore() {
    return new InMemoryTokenStore();
  }
}
