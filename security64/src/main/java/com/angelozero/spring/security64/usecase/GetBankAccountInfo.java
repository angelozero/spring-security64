package com.angelozero.spring.security64.usecase;

import com.angelozero.spring.security64.usecase.domain.BankAccount;
import org.springframework.stereotype.Service;

@Service
public class GetBankAccountInfo {

    public BankAccount execute(Integer id) {
        return switch (id) {
            case 1 -> new BankAccount(id, "angelo", "123", 543);
            case 2 -> new BankAccount(id, "jake", "456", 987);
            default -> new BankAccount(id, "", "", 0);
        };
    }
}
