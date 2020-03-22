package ru.itmo.republicn.react.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
public class Product {

    @Id
    String name;
    Double priceInRubles;

    public Double getPriceInRubles() {
        return priceInRubles;
    }

    public void setPriceInRubles(Double priceInRubles) {
        this.priceInRubles = priceInRubles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
