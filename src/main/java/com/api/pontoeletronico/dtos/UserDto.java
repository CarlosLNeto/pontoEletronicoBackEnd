package com.api.pontoeletronico.dtos;

import jakarta.validation.constraints.NotBlank;

public class UserDto {
    
    @NotBlank
    private String name;
    @NotBlank
    private String registration;
    @NotBlank
    private String login;
    @NotBlank
    private String password;


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRegistration() {
        return registration;
    }
    public void setRegistration(String registration) {
        this.registration = registration;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    
   
}
