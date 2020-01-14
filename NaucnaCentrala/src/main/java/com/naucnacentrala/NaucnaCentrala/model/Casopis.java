package com.naucnacentrala.NaucnaCentrala.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Casopis {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "naziv", nullable = false)
    private String naziv;

    @Column(name = "issn", nullable = false)
    private String issn;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "casopis_nobl",
            joinColumns = @JoinColumn(name = "casopis_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "nau_obl_id", referencedColumnName = "sifra"))
    private List<NaucnaOblast> naucneOblasti;

    @Column(name = "clanarina", nullable = false)
    private String clanarina;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "casopis_recenzenti",
            joinColumns = @JoinColumn(name = "casopis_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "recenzent", referencedColumnName = "username"))
    private List<Recenzent> recenzenti;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "urednik_id", nullable = false)
    private Urednik glavniUrednik;

    @OneToMany(mappedBy = "casopis", fetch = FetchType.LAZY)
    private List<Urednik> uredniciNO;

    @Column(name = "aktivan")
    private boolean aktivan;

    public Long getId() {
        return id;
    }

    public String getNaziv() {
        return naziv;
    }

    public String getIssn() {
        return issn;
    }

    public String getClanarina() {
        return clanarina;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public void setClanarina(String clanarina) {
        this.clanarina = clanarina;
    }

    public List<NaucnaOblast> getNaucneOblasti() {
        return naucneOblasti;
    }

    public void setNaucneOblasti(List<NaucnaOblast> naucneOblasti) {
        this.naucneOblasti = naucneOblasti;
    }

    public List<Recenzent> getRecenzenti() {
        return recenzenti;
    }

    public Urednik getGlavniUrednik() {
        return glavniUrednik;
    }

    public List<Urednik> getUredniciNO() {
        return uredniciNO;
    }

    public void setRecenzenti(List<Recenzent> recenzenti) {
        this.recenzenti = recenzenti;
    }

    public void setGlavniUrednik(Urednik glavniUrednik) {
        this.glavniUrednik = glavniUrednik;
    }

    public void setUredniciNO(List<Urednik> uredniciNO) {
        this.uredniciNO = uredniciNO;
    }

    public boolean isAktivan() {
        return aktivan;
    }

    public void setAktivan(boolean aktivan) {
        this.aktivan = aktivan;
    }

}
