package com.naucnacentrala.NaucnaCentrala.model;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("Recenzent")
public class Recenzent extends User {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "casopis_recenzenti")
    private List<Casopis> casopisi;

    @ManyToMany(mappedBy = "recenzenti", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<NaucniRad> naucniRadovi;

    public Recenzent(User user){
        super(user);
    }

    public Recenzent() {
        super();
    }

    public List<NaucniRad> getNaucniRadovi() {
        return naucniRadovi;
    }

    public void setNaucniRadovi(List<NaucniRad> naucniRadovi) {
        this.naucniRadovi = naucniRadovi;
    }

    public List<Casopis> getCasopisi() {
        return casopisi;
    }

    public void setCasopisi(List<Casopis> casopisi) {
        this.casopisi = casopisi;
    }
}
