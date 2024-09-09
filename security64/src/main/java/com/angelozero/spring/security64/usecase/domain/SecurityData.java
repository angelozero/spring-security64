package com.angelozero.spring.security64.usecase.domain;

import lombok.Data;

@Data
public abstract class SecurityData {
    private String owner;
    private Integer id;
    private String accountNumber;
    private double balance;
}
