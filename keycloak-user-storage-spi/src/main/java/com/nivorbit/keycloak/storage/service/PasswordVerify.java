package com.nivorbit.keycloak.storage.service;

import org.apache.commons.lang3.StringUtils;

public class PasswordVerify {
  public boolean verify(String input, String password) {
    return StringUtils.equals(input, password);
  }
}
