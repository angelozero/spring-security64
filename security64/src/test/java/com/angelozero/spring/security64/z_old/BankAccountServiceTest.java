package com.angelozero.spring.security64.z_old;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authorization.AuthorizationDeniedException;
//import org.junit.jupiter.api.AfterEach;
//import org.springframework.security.authentication.TestingAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@SpringBootTest
public class BankAccountServiceTest {

    private final BankAccountInterface service = new BankAccountServiceProxy(new BankAccountService());

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
}
