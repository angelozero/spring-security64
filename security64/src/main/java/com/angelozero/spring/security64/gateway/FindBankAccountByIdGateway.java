package com.angelozero.spring.security64.gateway;


import com.angelozero.spring.security64.usecase.domain.BankAccount;
import com.angelozero.spring.security64.gateway.annotation.PostCheckBankAccountOwner;

public interface FindBankAccountByIdGateway {

    @PostCheckBankAccountOwner
    BankAccount execute(Integer id);
}
