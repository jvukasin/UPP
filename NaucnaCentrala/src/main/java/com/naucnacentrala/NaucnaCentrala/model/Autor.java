package com.naucnacentrala.NaucnaCentrala.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ime", nullable = false)
    private String ime;

    @Column(name = "prezime", nullable = false)
    private String prezime;

    @Column(name = "grad", nullable = false)
    private String grad;

    @Column(name = "drzava", nullable = false)
    private String drzava;

    @Column(name = "email", nullable = false)
    private String email;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "autor_nobl",
            joinColumns = @JoinColumn(name = "autor_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "nau_obl_id", referencedColumnName = "sifra"))
    private List<NaucnaOblast> naucneOblasti;

    public Long getId() {
        return id;
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public String getGrad() {
        return grad;
    }

    public String getDrzava() {
        return drzava;
    }

    public String getEmail() {
        return email;
    }

    public List<NaucnaOblast> getNaucneOblasti() {
        return naucneOblasti;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNaucneOblasti(List<NaucnaOblast> naucneOblasti) {
        this.naucneOblasti = naucneOblasti;
    }
}
