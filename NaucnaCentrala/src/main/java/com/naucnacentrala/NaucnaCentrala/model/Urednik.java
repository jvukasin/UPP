package com.naucnacentrala.NaucnaCentrala.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("Urednik")
public class Urednik extends User {

    @ManyToOne(fetch = FetchType.LAZY)
    private Casopis casopis;

    public Casopis getCasopis() {
        return casopis;
    }

    public void setCasopis(Casopis casopis) {
        this.casopis = casopis;
    }
}
