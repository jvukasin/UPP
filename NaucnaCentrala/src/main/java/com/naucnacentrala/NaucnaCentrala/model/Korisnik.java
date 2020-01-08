package com.naucnacentrala.NaucnaCentrala.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Korisnik {

    @Id
    private String username;

    @Column(name = "ime", nullable = false)
    private String ime;

    @Column(name = "prezime", nullable = false)
    private String prezime;

    @Column(name = "grad", nullable = false)
    private String grad;

    @Column(name = "drzava", nullable = false)
    private String drzava;

    @Column(name = "titula")
    private String titula;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "uloga", nullable = false)
    private String uloga;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "active", nullable = false)
    private boolean aktivan;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "korisnik_nobl",
            joinColumns = @JoinColumn(name = "korisnik_id", referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(name = "nau_obl_id", referencedColumnName = "sifra"))
    private List<NaucnaOblast> naucneOblasti;

    public Korisnik() {

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

    public String getUloga() {
        return uloga;
    }

    public void setUloga(String uloga) {
        this.uloga = uloga;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAktivan() {
        return aktivan;
    }

    public void setAktivan(boolean aktivan) {
        this.aktivan = aktivan;
    }

    public String getTitula() {
        return titula;
    }

    public void setTitula(String titula) {
        this.titula = titula;
    }
}
