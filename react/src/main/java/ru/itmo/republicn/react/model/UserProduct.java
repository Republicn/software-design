package ru.itmo.republicn.react.model;

public class UserProduct {

    String name;
    String userId;
    Double userPrice;

    public UserProduct(String name, String userId, Double userPrice) {
        this.name = name;
        this.userId = userId;
        this.userPrice = userPrice;
    }
}