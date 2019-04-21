package com.example.hnl.myapplication.item_class;

import java.util.List;

public class Request {
    private String Phone;
    private String Name;
    private String Total;
    private List<Order> Shoes;
    private String key_order;

    public Request(String phone, String name, String total, List<Order> shoes, String key_order) {
        Phone = phone;
        Name = name;
        Total = total;
        Shoes = shoes;
        this.key_order = key_order;
    }

    public Request() {
    }

    public String getKey_order() {
        return key_order;
    }

    public void setKey_order(String key_order) {
        this.key_order = key_order;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public List<Order> getShoes() {
        return Shoes;
    }

    public void setShoes(List<Order> shoes) {
        Shoes = shoes;
    }
}
