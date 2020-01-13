package com.naucnacentrala.NaucnaCentrala.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Autor")
public class Autor extends User {
}
