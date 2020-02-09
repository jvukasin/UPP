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
            joinColumns = @JoinColumn(name = "casopisi_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "naucna_oblast_sifra", referencedColumnName = "sifra"))
    private List<NaucnaOblast> naucneOblasti;

    @Column(name = "clanarina", nullable = false)
    private String clanarina;

    @Column(name = "is_registered")
    private boolean isRegistered = false;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "casopis_recenzenti",
            joinColumns = @JoinColumn(name = "casopisi_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "recenzent_username", referencedColumnName = "username"))
    private List<Recenzent> recenzenti;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "urednik_id", nullable = false)
    private Urednik glavniUrednik;

    @OneToMany(mappedBy = "casopis", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Urednik> uredniciNO;

    @Column(name = "aktivan")
    private boolean aktivan;

    @Column(name = "seller_id")
    private Long sellerId;

    @OneToMany(mappedBy = "magazine", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderObject> orderObjects;

    @OneToMany(mappedBy = "magazine", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Subscription> subscriptions;

    @OneToMany(mappedBy = "magazine", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<NaucniRad> sciencePapers;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "casopis_korisnici_clanarine",
            joinColumns = @JoinColumn(name = "casopisi_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "clanarina_id", referencedColumnName = "id"))
    private List<Clanarina> korisniciSaClanarinom;

    public Casopis() {
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public List<NaucnaOblast> getNaucneOblasti() {
        return naucneOblasti;
    }

    public void setNaucneOblasti(List<NaucnaOblast> naucneOblasti) {
        this.naucneOblasti = naucneOblasti;
    }

    public String getClanarina() {
        return clanarina;
    }

    public void setClanarina(String clanarina) {
        this.clanarina = clanarina;
    }

    public List<Recenzent> getRecenzenti() {
        return recenzenti;
    }

    public void setRecenzenti(List<Recenzent> recenzenti) {
        this.recenzenti = recenzenti;
    }

    public Urednik getGlavniUrednik() {
        return glavniUrednik;
    }

    public void setGlavniUrednik(Urednik glavniUrednik) {
        this.glavniUrednik = glavniUrednik;
    }

    public List<Urednik> getUredniciNO() {
        return uredniciNO;
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

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public List<OrderObject> getOrderObjects() {
        return orderObjects;
    }

    public void setOrderObjects(List<OrderObject> orderObjects) {
        this.orderObjects = orderObjects;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<NaucniRad> getSciencePapers() {
        return sciencePapers;
    }

    public void setSciencePapers(List<NaucniRad> sciencePapers) {
        this.sciencePapers = sciencePapers;
    }

    public List<Clanarina> getKorisniciSaClanarinom() {
        return korisniciSaClanarinom;
    }

    public void setKorisniciSaClanarinom(List<Clanarina> korisniciSaClanarinom) {
        this.korisniciSaClanarinom = korisniciSaClanarinom;
    }


}
