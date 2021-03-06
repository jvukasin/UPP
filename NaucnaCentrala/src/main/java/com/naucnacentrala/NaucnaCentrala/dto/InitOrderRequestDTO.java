package com.naucnacentrala.NaucnaCentrala.dto;

import com.naucnacentrala.NaucnaCentrala.enums.Enums;

public class InitOrderRequestDTO {

    private Long ncOrderId;
    private String title;
    private String currency;
    private Long sellerId;
    private double amount;
    private String returnUrl;
    private Enums.OrderType orderType;
    private Enums.OrderStatus orderStatus;
    private String username;

    public InitOrderRequestDTO(){}

    public InitOrderRequestDTO(Long ncOrderId, String title, String currency, Long sellerId, double amount, String returnUrl, Enums.OrderType orderType, Enums.OrderStatus orderStatus, String username) {
        this.ncOrderId = ncOrderId;
        this.title = title;
        this.currency = currency;
        this.sellerId = sellerId;
        this.amount = amount;
        this.returnUrl = returnUrl;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.username = username;
    }

    public Long getNcOrderId() {
        return ncOrderId;
    }

    public void setNcOrderId(Long ncOrderId) {
        this.ncOrderId = ncOrderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public Enums.OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(Enums.OrderType orderType) {
        this.orderType = orderType;
    }

    public Enums.OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Enums.OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}