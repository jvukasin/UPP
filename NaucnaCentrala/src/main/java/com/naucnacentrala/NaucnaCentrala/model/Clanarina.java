package com.naucnacentrala.NaucnaCentrala.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Clanarina {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String username;

    @Column
    private String endDate;

    @Column
    private String chosenSubscription;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "casopis_korisnici_clanarine")
    private List<Casopis> casopisi;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<Casopis> getCasopisi() {
        return casopisi;
    }

    public void setCasopisi(List<Casopis> casopisi) {
        this.casopisi = casopisi;
    }

    public String getChosenSubscription() {
        return chosenSubscription;
    }

    public void setChosenSubscription(String chosenSubscription) {
        this.chosenSubscription = chosenSubscription;
    }
}
