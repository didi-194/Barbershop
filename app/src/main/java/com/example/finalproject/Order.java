package com.example.finalproject;

public class Order {
    String salon_name, barber_name, time_book, date_book, service_selected, price;
    int imageId;

    public Order(){}

    public Order(String salon_name, String barber_name, String time_book, String date_book, String service_selected, int imageId, String price) {
        this.salon_name = salon_name;
        this.barber_name = barber_name;
        this.time_book = time_book;
        this.date_book = date_book;
        this.service_selected = service_selected;
        this.imageId = imageId;
        this.price = price;
    }

    public String getSalon_name() {
        return salon_name;
    }

    public String getBarber_name() {
        return barber_name;
    }

    public String getTime_book() {
        return time_book;
    }

    public String getDate_book() {
        return date_book;
    }

    public String getService_selected() {
        return service_selected;
    }

    public int getImageId() {
        return imageId;
    }

    public String getPrice() {
        return price;
    }
}
