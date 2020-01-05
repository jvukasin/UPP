package com.naucnacentrala.NaucnaCentrala.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public abstract class Casopis {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "naziv", nullable = false)
    private String naziv;

    @Column(name = "issn", nullable = false)
    private long issn;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "casopis_nobl",
            joinColumns = @JoinColumn(name = "casopis_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "nau_obl_id", referencedColumnName = "sifra"))
    private List<NaucnaOblast> naucneOblasti;

    @Column(name = "clanarina", nullable = false)
    private String clanarina;

    //urednici i recenzenti

    public Long getId() {
        return id;
    }

    public String getNaziv() {
        return naziv;
    }

    public long getIssn() {
        return issn;
    }

    public String getClanarina() {
        return clanarina;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public void setIssn(long issn) {
        this.issn = issn;
    }

    public void setClanarina(String clanarina) {
        this.clanarina = clanarina;
    }

    public List<NaucnaOblast> getNaucneOblasti() {
        return naucneOblasti;
    }

    public void setNaucneOblasti(List<NaucnaOblast> naucneOblasti) {
        this.naucneOblasti = naucneOblasti;
    }
}
