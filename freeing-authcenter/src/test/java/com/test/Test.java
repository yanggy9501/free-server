package com.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author yanggy
 */
public class Test {

    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode("123456");
        System.out.println(passwordEncoder.matches("123456", "$2a$10$L85qFdUZZGoJbKkyoBEypuiRoNXgkW5pBBrp7UxzoDKYFNNVQnnXS"));
    }
}
