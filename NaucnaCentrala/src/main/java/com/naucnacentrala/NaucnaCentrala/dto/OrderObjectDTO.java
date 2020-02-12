package com.naucnacentrala.NaucnaCentrala.dto;


import com.naucnacentrala.NaucnaCentrala.enums.Enums;

public class OrderObjectDTO {

    private long id;
    private String userId;
    private double amount;
    private String orderType;
    private String orderStatus;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(Enums.OrderType orderType) {
        if(orderType.equals("ORDER_CASOPIS")) {
            this.orderType = "Casopis";
        } else if (orderType.equals("ORDER_RAD")) {
            this.orderType = "Rad";
        } else {
            this.orderType = "Pretplata";
        }
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Enums.OrderStatus orderStatus) {
        if(orderStatus.toString().equals("SUCCESS")) {
            this.orderStatus = "Uspešno";
        } else if (orderStatus.toString().equals("FAILED")) {
            this.orderStatus = "Neuspešno";
        } else {
            this.orderStatus = "U toku";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
