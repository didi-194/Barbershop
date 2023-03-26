package com.example.finalproject;

public class Service {
    int pic;
    String name, price;

    public Service(int pic, String name, String price) {
        this.pic = pic;
        this.name = name;
        this.price = price;
    }

    public int getPic() {
        return pic;
    }

    public String getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
}
