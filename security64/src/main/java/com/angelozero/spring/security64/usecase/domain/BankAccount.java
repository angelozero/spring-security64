package com.angelozero.spring.security64.usecase.domain;

import com.angelozero.spring.security64.config.handler.MaskAuthorizationDeniedHandler;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.method.HandleAuthorizationDenied;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize(as = BankAccount.class)
public class BankAccount {

    private Integer id;
    private String owner;
    private String accountNumber;
    private double balance;

    @PreAuthorize("this.owner == authentication?.name")
    @HandleAuthorizationDenied(handlerClass = MaskAuthorizationDeniedHandler.class)
    public String getAccountNumber() {
        return accountNumber;
    }
}
