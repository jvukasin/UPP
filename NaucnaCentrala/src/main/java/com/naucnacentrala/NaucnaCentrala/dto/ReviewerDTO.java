package com.naucnacentrala.NaucnaCentrala.dto;

public class ReviewerDTO {

    String username;
    String name;

    public ReviewerDTO() {
    }

    public ReviewerDTO(String username, String name) {
        this.username = username;
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
