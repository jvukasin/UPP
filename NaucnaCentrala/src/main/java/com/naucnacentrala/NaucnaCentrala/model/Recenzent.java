package com.naucnacentrala.NaucnaCentrala.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Recenzent")
public class Recenzent extends User {

    public Recenzent(User user){
        super(user);
    }

    public Recenzent() {

    }

}
