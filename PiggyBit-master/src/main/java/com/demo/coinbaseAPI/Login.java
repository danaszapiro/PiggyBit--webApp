package com.demo.coinbaseAPI;

public class Login{
    public String username;
    public String password;

    protected Login() {
    }

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public authenticate(Login loginSession){
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
