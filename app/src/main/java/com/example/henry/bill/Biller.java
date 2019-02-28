package com.example.henry.bill;

import org.litepal.crud.DataSupport;

public class Biller extends DataSupport {
    private String cost;
    private String sort;
    private int year;
    private int month;
    private int day;
    private int id;
    private String  pay_or_get;
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getPay_or_get() {
        return pay_or_get;
    }

    public void setPay_or_get(String pay_or_get) {
        this.pay_or_get = pay_or_get;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

}
