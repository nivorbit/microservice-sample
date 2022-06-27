package com.nivorbit.apigateway.security;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

@Slf4j
@Component
public class BearerTokenResolver {

  private static final Pattern
      authorizationPattern = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-._~+/]+=*)$", 2);

  public String resolve(ServerWebExchange exchange) {
    return resolveFromAuthorizationHeader(exchange);
  }

  private String resolveFromAuthorizationHeader(ServerWebExchange exchange) {
    List<String> authorizations = exchange.getRequest().getHeaders().get("Authorization");

    if(CollectionUtils.isEmpty(authorizations)) {
      return "";
    }

    if (!StringUtils.startsWithIgnoreCase(authorizations.get(0), "bearer")) {
      return authorizations.get(0);
    } else {
      Matcher matcher = authorizationPattern.matcher(authorizations.get(0));
      if (!matcher.matches()) {
        return authorizations.get(0);
      } else {
        return matcher.group("token");
      }
    }
  }
}
