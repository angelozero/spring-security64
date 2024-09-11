package com.angelozero.spring.security64.zzz;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser("angelo")
public @interface WithMockUserAngelo {
}
