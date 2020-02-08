package com.naucnacentrala.NaucnaCentrala.dto;

public class CasopisInfoDTO {

    private String name;
    private String issn;
    private String currency;
    private double amount;
    private long sellerId;

    public CasopisInfoDTO() {
    }

    public CasopisInfoDTO(String name, String issn, String currency, double amount, long sellerId) {
        this.name = name;
        this.issn = issn;
        this.currency = currency;
        this.amount = amount;
        this.sellerId = sellerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getCurrency() {
        return currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    public long getSellerId() {
        return sellerId;
    }
}
