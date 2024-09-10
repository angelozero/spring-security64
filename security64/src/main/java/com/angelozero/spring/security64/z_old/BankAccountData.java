package com.angelozero.spring.security64.z_old;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountData {

    private Integer id;
    private String owner;
    private String accountNumber;
    private Double balance;
}
