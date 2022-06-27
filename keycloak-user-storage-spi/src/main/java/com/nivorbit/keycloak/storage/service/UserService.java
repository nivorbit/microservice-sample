package com.nivorbit.keycloak.storage.service;

import com.nivorbit.keycloak.storage.model.User;
import java.util.HashMap;
import java.util.Map;

public class UserService {

  public static final String PASSWORD = "test1234";
  private Map<String, User> storage;

  public UserService() {
    this.storage = new HashMap<>();
    this.storage.put("user", new User("user", PASSWORD));
    this.storage.put("admin", new User("admin", PASSWORD));
    this.storage.put("test", new User("test", PASSWORD));
  }

  public User findByUsername(String username) {
    return this.storage.get(username);
  }
}
