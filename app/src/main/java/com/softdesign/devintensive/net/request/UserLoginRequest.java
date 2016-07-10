package com.softdesign.devintensive.net.request;

/**
 * Created by savos on 10.07.2016.
 */
public class UserLoginRequest {

    private String email;
    private String password;

    public UserLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
