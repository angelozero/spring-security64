package com.angelozero.spring.security64.usecase;

import com.angelozero.spring.security64.config.WithMockAngelo;
import com.angelozero.spring.security64.config.WithMockJake;
import com.angelozero.spring.security64.gateway.SaveBankAccountGateway;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.authorization.AuthorizationProxyFactory;
import org.springframework.security.authorization.method.AuthorizationAdvisorProxyFactory;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SaveBankAccountTest {

    private final GetBankAccountInfo getBankAccountInfo = new GetBankAccountInfo();

    private final AuthorizationProxyFactory factory =
            AuthorizationAdvisorProxyFactory.withDefaults();

    private final SaveBankAccountGateway saveBankAccount = (SaveBankAccountGateway) factory.proxy(new SaveBankAccount());


    @Test
    @WithMockAngelo
    void shouldSaveBankAccountWithSuccess() {
        this.saveBankAccount.execute(getBankAccountInfo.execute(1));
    }

    @Test
    @WithMockJake
    void shouldReceiveAccessDenied() {
        assertThatExceptionOfType(AuthorizationDeniedException.class)
                .isThrownBy(() -> this.saveBankAccount.execute(getBankAccountInfo.execute(1)))
                .withMessage("Access Denied");
    }
}
