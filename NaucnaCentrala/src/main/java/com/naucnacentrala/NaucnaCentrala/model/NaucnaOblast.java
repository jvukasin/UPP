package com.naucnacentrala.NaucnaCentrala.model;

import javax.persistence.*;

@Entity
public class NaucnaOblast {

    @Id
    private Long sifra;

    @Column(name = "naziv", nullable = false)
    private String naziv;

    public Long getSifra() {
        return sifra;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setSifra(Long sifra) {
        this.sifra = sifra;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
}
