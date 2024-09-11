package com.angelozero.spring.security64.zzz;

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
    public void saveBankAccount(BankAccountData bankAccountDataToSave) {
        System.out.println("Bank Account saved with success");
    }

    @Override
    public void updateBankAccount(BankAccountData bankAccountDataToUpdate) {
        System.out.println("Bank Account updated with success");
    }
}
