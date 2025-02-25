package com.csk.springboot.myfirstwebapp.login;

import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    public boolean authenticate(String username, String password) {
        boolean isValid = username.equalsIgnoreCase("in28minutes");
        boolean isValidPassword = password.equalsIgnoreCase("dummy");
        return isValid && isValidPassword;
    }
}
