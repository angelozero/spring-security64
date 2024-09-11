package com.angelozero.spring.security64.z_old;

import com.angelozero.spring.security64.usecase.domain.BankAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authorization.AuthorizationDeniedException;
//import org.springframework.security.authorization.AuthorizationProxyFactory;
//import org.springframework.security.authorization.method.AuthorizationAdvisorProxyFactory;
//import org.junit.jupiter.api.AfterEach;
//import org.springframework.security.authentication.TestingAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BankAccountServiceTest {

    @Autowired
    private BankAccountInterface service;

//    AuthorizationProxyFactory factory
//            = AuthorizationAdvisorProxyFactory.withDefaults();
//
//    private final BankAccountInterface service = (BankAccountInterface) factory.proxy(new BankAccountService());

//    private final BankAccountInterface service = new BankAccountServiceProxy(new BankAccountService());

//    @AfterEach
//    void cleanUp() {
//        SecurityContextHolder.clearContext();
//    }


//    private void login(String user) {
//        Authentication auth =
//                new TestingAuthenticationToken(user, "password", "ADMIN");
//        SecurityContextHolder.getContext().setAuthentication(auth);
//    }

    @Test
    @DisplayName("Should find the bank account with success")
    //@WithMockUser("angelo")
    @WithMockUserAngelo
    void findByIdGranted() {
        //login("angelo");
        service.findById(1);
    }

    @Test
    @DisplayName("Should get the bank account with success")
    //@WithMockUser("angelo")
    @WithMockUserAngelo
    void getByIdGranted() {
        //login("angelo");
        service.getById(1);
    }

    @Test
    @DisplayName("Should find the bank account with success with role ADMIN")
    @WithMockUserAdmin
    void findByIdWithAdminGranted() {
        service.findById(1);
    }

    @Test
    @DisplayName("Should fail to find the bank account")
    //@WithMockUser("jake")
    @WithMockUserJake
    void findByIdWhenDenied() {
        //login("jake");
        assertThatExceptionOfType(AuthorizationDeniedException.class)
                .isThrownBy(() -> service.findById(1))
                .withMessage("Access Denied");
    }

    @Test
    @DisplayName("Should fail to get the bank account")
    //@WithMockUser("jake")
    @WithMockUserJake
    void getByIdWhenDenied() {
        //login("jake");
        assertThatExceptionOfType(AuthorizationDeniedException.class)
                .isThrownBy(() -> service.getById(1))
                .withMessage("Access Denied");
    }

    @Test
    @DisplayName("Should fail to find the bank account with not admin role")
    @WithMockUserNotAdmin
    void findByIdWhenDeniedWithNotAdminRole() {
        assertThatExceptionOfType(AuthorizationDeniedException.class)
                .isThrownBy(() -> service.findById(1))
                .withMessage("Access Denied");
    }

    @Test
    @DisplayName("Should find the bank account with success see the account number")
    @WithMockUserAngelo
    void findByIdWithSuccessAndSeeTheAccountNumber() {
        var response = service.findById(1);
        assertEquals("1234", response.getAccountNumber());
    }

    @Test
    @DisplayName("Should find the bank account with success but cannot see the account number")
    @WithMockUserAdmin
    void findByIdWithSuccessButCannotSeeTheAccountNumber() {
        var response = service.findById(1);
        assertEquals("****", response.getAccountNumber());
    }

    @Test
    @DisplayName("Should save a bank account with success")
    @WithMockUserAngelo
    void shouldSaveABankAccountWithSuccess() {
        service.saveBankAccount(mockBankAccountAngelo());
    }

    @Test
    @DisplayName("Should not save a bank account")
    @WithMockUserJake
    void shouldSNotSaveABankAccount() {
        assertThatExceptionOfType(AuthorizationDeniedException.class)
                .isThrownBy(() -> service.saveBankAccount(mockBankAccountAngelo()))
                .withMessage("Access Denied");
    }

    private BankAccountData mockBankAccountAngelo() {
        return BankAccountData.builder()
                .id(1)
                .accountNumber("1234")
                .owner("angelo")
                .balance(1000D)
                .build();
    }
}