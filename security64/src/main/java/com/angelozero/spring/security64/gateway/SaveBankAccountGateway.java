package com.angelozero.spring.security64.gateway;


import com.angelozero.spring.security64.usecase.domain.BankAccount;
import com.angelozero.spring.security64.gateway.annotation.PreCheckBankAccountOwner;

public interface SaveBankAccountGateway {

    @PreCheckBankAccountOwner
    void execute(BankAccount bankAccount);
}
