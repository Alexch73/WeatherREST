package com.example.weatherrest.models;

public class Weather {
    private  String date;
    private String value;

    public  Weather(){
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
