# spring-security64
Spring Security 6.4

separando logica de autorizacao com logica de dominio

estrutura inicial

```java
//com.angelozero.spring.security64.domain;
public record BankAccount(Integer id, String owner, String accountNumber, double balance) {
}

/****/
//package com.angelozero.spring.security64.usecase;
import com.angelozero.spring.security64.domain.BankAccount;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class FindBankAccountById {

    public BankAccount execute(Integer id) {
        var account = getBankAccountInfo(id);
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        if (!principal.getName().equals(account.owner())) {
            throw new AuthorizationDeniedException("Access Denied", new AuthorizationDecision(false));
        }
        return account;
    }

    private BankAccount getBankAccountInfo(Integer id) {
        return switch (id) {
            case 1 -> new BankAccount(id, "angelo", "123", 543);
            case 2 -> new BankAccount(id, "jake", "456", 987);
            default -> throw new RuntimeException("User Not Found");
        };
    }
}

/****/
//package com.angelozero.spring.security64.entrypoint;
import com.angelozero.spring.security64.domain.BankAccount;
import com.angelozero.spring.security64.usecase.FindBankAccountById;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bank-account")
@AllArgsConstructor
public class BankAccountController {

    private final FindBankAccountById findBankAccountById;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BankAccount> findById(@PathVariable("id") Integer id) {
        var response = findBankAccountById.execute(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

/***/
//package com.angelozero.spring.security64.usecase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class FindBankAccountByIdTest {

    FindBankAccountById findBankAccountById = new FindBankAccountById();

    void login(String user) {
        var auth = switch (user) {
            case "angelo" -> new TestingAuthenticationToken(user, "password-123", "MASTER");
            case "jake" -> new TestingAuthenticationToken(user, "password-456", "TOP");
            default -> throw new RuntimeException("Test User not Found");
        };
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void cleanUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void findByIdWhenGranted() {
        login("angelo");
        this.findBankAccountById.execute(1);
    }

    @Test
    void findByIdWhenDenied() {
        login("jake");
        assertThatExceptionOfType(AuthorizationDeniedException.class)
                .isThrownBy(() -> this.findBankAccountById.execute(1))
                .withMessage("Access Denied");
    }
}

``` 