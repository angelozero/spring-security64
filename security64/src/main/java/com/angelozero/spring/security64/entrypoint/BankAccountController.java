package com.angelozero.spring.security64.entrypoint;

import com.angelozero.spring.security64.usecase.domain.BankAccount;
import com.angelozero.spring.security64.gateway.FindBankAccountByIdGateway;
import com.angelozero.spring.security64.gateway.SaveBankAccountGateway;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bank-account")
@AllArgsConstructor
public class BankAccountController {

    private final FindBankAccountByIdGateway findBankAccountById;
    private final SaveBankAccountGateway saveBankAccount;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BankAccount> findById(@PathVariable("id") Integer id) {
        var response = findBankAccountById.execute(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveBankAccount(@RequestBody BankAccount bankAccount) {
        saveBankAccount.execute(bankAccount);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
