package com.careerforge.careerforge.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashGen {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String[] passwords = {"au@123456", "NewPass@123"};
        for (String pwd : passwords) {
            System.out.println("Password: " + pwd + " => " + encoder.encode(pwd));
        }
    }
}
