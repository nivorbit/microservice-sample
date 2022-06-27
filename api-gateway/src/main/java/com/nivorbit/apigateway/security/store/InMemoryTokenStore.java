package com.nivorbit.apigateway.security.store;

import com.nivorbit.apigateway.security.TokenStore;
import java.util.HashMap;
import java.util.Map;

public class InMemoryTokenStore implements TokenStore {

  private final Map<String, String> store = new HashMap<>();


  @Override
  public String getToken(String token) {
    return store.get(token);
  }

  @Override
  public void store(String token, String exchangeToken) {
      store.put(token, exchangeToken);
  }
}
