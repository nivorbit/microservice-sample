package com.nivorbit.customerservice;

import com.nivorbit.auth.reactive.configurer.ReactiveServerHttpSecurityConfigurer;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.stereotype.Component;

@Component
public class ReactiveSecurityConfigurer implements ReactiveServerHttpSecurityConfigurer {

  @Override
  public void configure(ServerHttpSecurity.AuthorizeExchangeSpec exchange) {
    exchange
        .matchers(EndpointRequest.toAnyEndpoint())
        .permitAll();
  }
}
