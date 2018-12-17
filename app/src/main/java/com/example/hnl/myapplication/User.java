package com.example.hnl.myapplication;

public class User {
    private String name;
    private String Password;
    private Byte typeUser;

    public Byte getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(Byte typeUser) {
        this.typeUser = typeUser;
    }

    public User(String name, String password, Byte typeUser) {
        this.name = name;
        Password = password;
        this.typeUser = typeUser;

    }

    public User()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
