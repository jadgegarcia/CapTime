package com.example.captime;

public class UserHelper {
    private String fullname;
    private String username;
    private String password;
    private String email;

    public UserHelper(String email, String fullname, String username, String password) {
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public UserHelper() {
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
