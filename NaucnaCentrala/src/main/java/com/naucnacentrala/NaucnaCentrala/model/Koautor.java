package com.naucnacentrala.NaucnaCentrala.model;

import javax.persistence.*;

@Entity
public class Koautor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ime")
    private String ime;

    @Column(name = "email")
    private String email;

    @Column(name = "grad")
    private String grad;

    @Column(name = "drzava")
    private String drzava;

    @ManyToOne
    private NaucniRad naucniRad;

    public Koautor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public String getDrzava() {
        return drzava;
    }

    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }

    public NaucniRad getNaucniRad() {
        return naucniRad;
    }

    public void setNaucniRad(NaucniRad naucniRad) {
        this.naucniRad = naucniRad;
    }
}
