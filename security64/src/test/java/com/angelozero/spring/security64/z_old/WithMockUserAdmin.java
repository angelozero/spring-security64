package com.angelozero.spring.security64.z_old;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(roles = {"ADMIN"})
public @interface WithMockUserAdmin {
}
