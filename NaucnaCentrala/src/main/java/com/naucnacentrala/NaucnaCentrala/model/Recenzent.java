package com.naucnacentrala.NaucnaCentrala.model;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("Recenzent")
public class Recenzent extends User {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "casopis_recenzenti")
    private List<Casopis> casopisi;

    public Recenzent(User user){
        super(user);
    }

    public Recenzent() {

    }

}
