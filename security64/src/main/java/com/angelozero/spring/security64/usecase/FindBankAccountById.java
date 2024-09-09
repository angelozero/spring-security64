package com.angelozero.spring.security64.usecase;

import com.angelozero.spring.security64.usecase.domain.BankAccount;
import com.angelozero.spring.security64.gateway.FindBankAccountByIdGateway;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FindBankAccountById implements FindBankAccountByIdGateway {

    private final GetBankAccountInfo getBankAccountInfo;

    @Override
    public BankAccount execute(Integer id) {
        return getBankAccountInfo.execute(id);
    }
}
