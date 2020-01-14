package com.naucnacentrala.NaucnaCentrala.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class NacinPlacanja {

    @Id
    private Long id;

    @Column(name = "naziv", nullable = false)
    private String naziv;

    public Long getId() {
        return id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
}
