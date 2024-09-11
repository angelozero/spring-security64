package com.angelozero.spring.security64.z_old;


import com.angelozero.spring.security64.config.handler.MaskAuthorizationDeniedHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.method.HandleAuthorizationDenied;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountData {

    private Integer id;
    private String owner;
    private String accountNumber;
    private Double balance;


    @PreAuthorize("this.owner == authentication?.name")
    @HandleAuthorizationDenied(handlerClass = MaskAuthorizationDeniedHandler.class)
    public String getAccountNumber() {
        return accountNumber;
    }
}
