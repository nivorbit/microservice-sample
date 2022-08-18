package com.nivorbit.auth;

import com.nivorbit.auth.reactive.configuration.ReactiveAuthConfiguration;
import com.nivorbit.auth.servlet.configuration.ServletAuthConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnProperty(value = "auth.enabled", matchIfMissing = true)
@Import({ServletAuthConfiguration.class, ReactiveAuthConfiguration.class})
public class AuthAutoConfiguration { }
