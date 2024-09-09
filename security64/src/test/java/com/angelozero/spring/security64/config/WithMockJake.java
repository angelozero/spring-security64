package com.angelozero.spring.security64.config;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(value = "jake", roles = {"TOP"})
public @interface WithMockJake {
}
