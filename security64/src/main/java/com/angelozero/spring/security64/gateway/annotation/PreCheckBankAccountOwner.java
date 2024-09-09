package com.angelozero.spring.security64.gateway.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@PreAuthorize("#bankAccount?.owner == authentication?.name")
@Retention(RetentionPolicy.RUNTIME)
public @interface PreCheckBankAccountOwner {
}
