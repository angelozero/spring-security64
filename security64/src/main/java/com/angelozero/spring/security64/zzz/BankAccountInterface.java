package com.angelozero.spring.security64.zzz;

//import org.springframework.security.access.prepost.PostAuthorize;

public interface BankAccountInterface {

    //    @PostAuthorize("returnObject.?owner == authentication?.name")
    @PosReadBankAccount
    BankAccountData findById(Integer id);

    //    @PostAuthorize("returnObject.?owner == authentication?.name")
    @PosReadBankAccount
    BankAccountData getById(Integer id);

    @PreWriteBankAccount("#bankAccountDataToSave")
    void saveBankAccount(BankAccountData bankAccountDataToSave);

    @PreWriteBankAccount("#bankAccountDataToUpdate")
    void updateBankAccount(BankAccountData bankAccountDataToUpdate);
}
