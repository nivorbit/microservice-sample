package com.nivorbit.apigateway.security;

public interface TokenStore {
  String getToken(String token);
  void  store(String token, String exchangeToken);
}
