package com.nivorbit.auth.reactive.configuration;

import com.nivorbit.auth.reactive.configurer.ReactiveServerHttpSecurityConfigurer;
import java.util.Iterator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class ReactiveSecurityConfiguration {
  private final ReactiveJwtAuthenticationConverterAdapter reactiveJwtAuthenticationConverterAdapter;
  private final List<ReactiveServerHttpSecurityConfigurer> configurers;

  @Bean
  SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    http.authorizeExchange(
            exchange -> {
              if (configurers != null) {
                Iterator iterator = this.configurers.iterator();

                ReactiveServerHttpSecurityConfigurer configurer;
                while(iterator.hasNext()) {
                  configurer = (ReactiveServerHttpSecurityConfigurer) iterator.next();
                  configurer.configure(exchange);
                }
              }
              exchange
                  .anyExchange()
                  .authenticated();
            })
        .oauth2ResourceServer()
        .jwt()
        .jwtAuthenticationConverter(reactiveJwtAuthenticationConverterAdapter);

    return http.build();
  }
}
