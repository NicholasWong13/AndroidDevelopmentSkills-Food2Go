package com.example.food2go;

public class Admin {

    public String email;
    public String password;
    public String userName;

    public Admin(){

    }
    public Admin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Admin(String email, String password, String userName) {
        this.email = email;
        this.password = password;
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
