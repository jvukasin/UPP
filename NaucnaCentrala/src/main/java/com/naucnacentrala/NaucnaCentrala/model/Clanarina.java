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
    private long agreementID;

    @ManyToOne(fetch = FetchType.LAZY)
    private Casopis casopis;

    public Clanarina() {

    }

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

    public Casopis getCasopis() {
        return casopis;
    }

    public void setCasopis(Casopis casopis) {
        this.casopis = casopis;
    }

    public long getAgreementID() {
        return agreementID;
    }

    public void setAgreementID(long agreementID) {
        this.agreementID = agreementID;
    }
}
