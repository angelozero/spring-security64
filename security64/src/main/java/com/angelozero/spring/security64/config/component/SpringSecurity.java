package com.angelozero.spring.security64.config.component;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.method.PrePostTemplateDefaults;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SpringSecurity {

    @Bean
    PrePostTemplateDefaults prePostTemplateDefaults() {
        return new PrePostTemplateDefaults();
    }

    @Bean
    UserDetailsService userDetailsService() {
        UserDetails angelo = User.builder()
                .username("angelo")
                .password("{noop}password-123")
                .roles("MASTER")
                .build();

        UserDetails jake = User.builder()
                .username("jake")
                .password("{noop}password-456")
                .roles("TOP")
                .build();

        UserDetails dumb = User.builder()
                .username("dumb")
                .password("{noop}password-789")
                .roles("UP")
                .build();

        return new InMemoryUserDetailsManager(angelo, jake, dumb);
    }
}
