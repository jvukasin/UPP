package com.naucnacentrala.NaucnaCentrala.dto;

public class AdvancedSearchDTO {

    private String magazineName;
    private String title;
    private String keyTerms;
    private String text;
    private String author;
    private String scienceField;
    private String checkAndOr;

    public AdvancedSearchDTO() {
    }

    public AdvancedSearchDTO(String magazineName, String title, String keyTerms, String text, String author, String scienceField, String checkAndOr) {
        this.magazineName = magazineName;
        this.title = title;
        this.keyTerms = keyTerms;
        this.text = text;
        this.author = author;
        this.scienceField = scienceField;
        this.checkAndOr = checkAndOr;
    }

    public String getMagazineName() {
        return magazineName;
    }

    public void setMagazineName(String magazineName) {
        this.magazineName = magazineName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeyTerms() {
        return keyTerms;
    }

    public void setKeyTerms(String keyTerms) {
        this.keyTerms = keyTerms;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getScienceField() {
        return scienceField;
    }

    public void setScienceField(String scienceField) {
        this.scienceField = scienceField;
    }

    public String getCheckAndOr() {
        return checkAndOr;
    }

    public void setCheckAndOr(String checkAndOr) {
        this.checkAndOr = checkAndOr;
    }
}
