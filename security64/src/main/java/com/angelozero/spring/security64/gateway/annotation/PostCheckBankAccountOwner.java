package com.angelozero.spring.security64.gateway.annotation;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authorization.method.AuthorizeReturnObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@PostAuthorize("hasRole('MASTER') or hasRole('UP')")
@AuthorizeReturnObject
@Retention(RetentionPolicy.RUNTIME)
public @interface PostCheckBankAccountOwner {
}
