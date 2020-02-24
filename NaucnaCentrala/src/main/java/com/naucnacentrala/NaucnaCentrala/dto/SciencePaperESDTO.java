package com.naucnacentrala.NaucnaCentrala.dto;

public class SciencePaperESDTO {

    private Long id;
    private String title;
    private String paperAbstract;
    private String highlight;
    private boolean openAcess;
    private String magazineName;
    private String author;
    private double price;
    private String currency;

    public SciencePaperESDTO(Long id, String title, String paperAbstract, String highlight, boolean openAcess, String magazineName, String author, double price, String currency) {
        this.id = id;
        this.title = title;
        this.paperAbstract = paperAbstract;
        this.highlight = highlight;
        this.openAcess = openAcess;
        this.magazineName = magazineName;
        this.author = author;
        this.price = price;
        this.currency = currency;
    }

    public SciencePaperESDTO() {
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPaperAbstract() {
        return paperAbstract;
    }

    public void setPaperAbstract(String paperAbstract) {
        this.paperAbstract = paperAbstract;
    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public boolean isOpenAcess() {
        return openAcess;
    }

    public void setOpenAcess(boolean openAcess) {
        this.openAcess = openAcess;
    }

    public String getMagazineName() {
        return magazineName;
    }

    public void setMagazineName(String magazineName) {
        this.magazineName = magazineName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
