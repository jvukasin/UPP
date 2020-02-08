package com.naucnacentrala.NaucnaCentrala.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "star_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "user_id")
    private String userId;

    @ManyToOne
    private Casopis magazine;

    @OneToMany(mappedBy = "subscription", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderObject> orderObjects;

    public Subscription() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Casopis getMagazine() {
        return magazine;
    }

    public void setMagazine(Casopis magazine) {
        this.magazine = magazine;
    }

    public List<OrderObject> getOrderObjects() {
        return orderObjects;
    }

    public void setOrderObjects(List<OrderObject> orderObjects) {
        this.orderObjects = orderObjects;
    }
}
