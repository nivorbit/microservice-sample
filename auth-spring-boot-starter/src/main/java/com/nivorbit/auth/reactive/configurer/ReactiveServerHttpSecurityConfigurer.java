package com.nivorbit.auth.reactive.configurer;

import org.springframework.security.config.web.server.ServerHttpSecurity;

public interface ReactiveServerHttpSecurityConfigurer {
  void configure(ServerHttpSecurity.AuthorizeExchangeSpec http);
}
