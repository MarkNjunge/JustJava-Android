package com.marknkamau.justjava.models;

public class UserDefaults {
    private String name;
    private String phone;
    private String defaultAddress;

    public UserDefaults(String name, String phone, String defaultAddress) {
        this.name = name;
        this.phone = phone;
        this.defaultAddress = defaultAddress;
    }


    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getDefaultAddress() {
        return defaultAddress;
    }
}
