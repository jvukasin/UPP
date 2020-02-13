package com.naucnacentrala.NaucnaCentrala.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class NaucnaOblast {

    @Id
    private Long sifra;

    @Column(name = "naziv", nullable = false)
    private String naziv;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "casopis_nobl")
    private List<Casopis> casopisi;

    public NaucnaOblast() {

    }

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

    public List<Casopis> getCasopisi() {
        return casopisi;
    }

    public void setCasopisi(List<Casopis> casopisi) {
        this.casopisi = casopisi;
    }
}
