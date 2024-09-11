package com.angelozero.spring.security64.z_old;

import com.angelozero.spring.security64.usecase.domain.BankAccount;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService implements BankAccountInterface {

    @Override
    public BankAccountData findById(Integer id) {
        return BankAccountData.builder()
                .id(id)
                .accountNumber("1234")
                .owner("angelo")
                .balance(1000D)
                .build();
    }

    @Override
    public BankAccountData getById(Integer id) {
        return findById(id);
    }

    @Override
    public void saveBankAccount(BankAccount bankAccountToSave) {

    }
}
