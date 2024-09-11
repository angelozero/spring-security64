package com.angelozero.spring.security64.z_old;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authorization.method.AuthorizeReturnObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PostAuthorize("returnObject?.owner == authentication?.name or hasRole('ADMIN')")
@AuthorizeReturnObject
public @interface PosReadBankAccount {
}
