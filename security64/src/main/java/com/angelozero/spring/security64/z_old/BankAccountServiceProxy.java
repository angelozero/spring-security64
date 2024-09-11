package com.angelozero.spring.security64.z_old;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Deprecated
@Service
@AllArgsConstructor
public class BankAccountServiceProxy /*implements BankAccountInterface*/ {

    @Autowired
    private BankAccountService service;

    //    @Override
    public BankAccountData findById(Integer id) {
//        var bankAccount = service.findById(id);
//
//        Principal principal = SecurityContextHolder.getContext().getAuthentication();
//
//        if (!principal.getName().equals(bankAccount.getOwner())) {
//            throw new AuthorizationDeniedException("Access Denied", new AuthorizationDecision(false));
//        }
//
//        return bankAccount;
        return service.findById(id);
    }

    //    @Override
    public BankAccountData getById(Integer id) {
//        var bankAccount = service.findById(id);
//        Principal principal = SecurityContextHolder.getContext().getAuthentication();
//
//        if (!principal.getName().equals(bankAccount.getOwner())) {
//            throw new AuthorizationDeniedException("Access Denied", new AuthorizationDecision(false));
//        }
//
//        return bankAccount;
        return service.findById(id);
    }
}
