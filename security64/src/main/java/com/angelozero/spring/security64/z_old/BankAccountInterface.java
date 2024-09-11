package com.angelozero.spring.security64.z_old;

//import org.springframework.security.access.prepost.PostAuthorize;

import com.angelozero.spring.security64.usecase.domain.BankAccount;

public interface BankAccountInterface {

    //    @PostAuthorize("returnObject.?owner == authentication?.name")
    @PosReadBankAccount
    BankAccountData findById(Integer id);

    //    @PostAuthorize("returnObject.?owner == authentication?.name")
    @PosReadBankAccount
    BankAccountData getById(Integer id);

    void saveBankAccount(BankAccountData bankAccountDataToSave);
}
