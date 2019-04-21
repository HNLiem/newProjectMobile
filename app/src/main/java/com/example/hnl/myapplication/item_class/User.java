package com.example.hnl.myapplication.item_class;

public class User {
    private String name;
    private String Password;
    private String Phone;
    private String type;

    public User(String name, String password, String phone, String type) {
        this.name = name;
        Password = password;
        Phone = phone;
        this.type = type;
    }
    public User() { }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

}
