package com.nivorbit.auth.servlet.configuration;

import com.nivorbit.auth.servlet.configurer.ServerHttpSecurityConfigurer;
import java.util.Iterator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final List<ServerHttpSecurityConfigurer> configurers;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.authorizeRequests(
            authorize -> {
              if (configurers != null) {
                Iterator iterator = this.configurers.iterator();

                ServerHttpSecurityConfigurer configurer;
                while(iterator.hasNext()) {
                  configurer = (ServerHttpSecurityConfigurer) iterator.next();
                  configurer.configure(authorize);
                }
              }

              authorize
                  .anyRequest()
                  .authenticated();
            })
        .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
        .build();
  }


}
