package com.angelozero.spring.security64.usecase;

import com.angelozero.spring.security64.config.WithMockAngelo;
import com.angelozero.spring.security64.config.WithMockDumb;
import com.angelozero.spring.security64.config.WithMockJake;
import com.angelozero.spring.security64.gateway.FindBankAccountByIdGateway;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.authorization.AuthorizationProxyFactory;
import org.springframework.security.authorization.method.AuthorizationAdvisorProxyFactory;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FindBankAccountByIdTest {

    AuthorizationProxyFactory factory =
            AuthorizationAdvisorProxyFactory.withDefaults();

    FindBankAccountByIdGateway findBankAccountById = (FindBankAccountByIdGateway) factory.proxy(new FindBankAccountById(new GetBankAccountInfo()));


    @Test
    @WithMockAngelo
    void shouldFindBankAccountWithSuccess() {
        this.findBankAccountById.execute(1);
    }

    @Test
    @WithMockAngelo
    void shouldFindBankAccountRoleMasterWithSuccess() {
        var response = this.findBankAccountById.execute(1);
        assertEquals("123", response.getAccountNumber());
    }

    @Test
    @WithMockDumb
    void shouldFindBankAccountRoleUpWithSuccess() {
        var response = this.findBankAccountById.execute(1);
        assertEquals("****", response.getAccountNumber());
    }

    @Test
    @WithMockJake
    void shouldReceiveAccessDenied() {
        assertThatExceptionOfType(AuthorizationDeniedException.class)
                .isThrownBy(() -> this.findBankAccountById.execute(1))
                .withMessage("Access Denied");
    }
}
