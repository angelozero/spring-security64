package com.angelozero.spring.security64.usecase;

import com.angelozero.spring.security64.usecase.domain.BankAccount;
import com.angelozero.spring.security64.gateway.SaveBankAccountGateway;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SaveBankAccount implements SaveBankAccountGateway {

    @Override
    public void execute(BankAccount bankAccount) {
        System.out.println(STR."Bank Account: \{bankAccount.getAccountNumber()} saved with success");
    }
}
