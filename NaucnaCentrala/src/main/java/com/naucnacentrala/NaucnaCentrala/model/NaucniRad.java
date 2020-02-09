package com.naucnacentrala.NaucnaCentrala.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class NaucniRad {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "key_term")
    private String keyTerm;

    @Column(name = "paper_abstract")
    private String paperAbstract;

    @ManyToOne
    private NaucnaOblast scienceField;

    @ManyToOne
    private Casopis magazine;

    @OneToMany(mappedBy = "sciencePaper", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderObject> orderObjects;

    @Column(name = "currency")
    private String currency;

    @Column(name = "price")
    private double price;

    @Column(name = "pdf")
    @Lob
    private byte[] pdf;

    @Column(name = "pdf_name")
    private String pdfName;

    @OneToMany(mappedBy = "naucniRad", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Koautor> koautori;

    public NaucniRad() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeyTerm() {
        return keyTerm;
    }

    public void setKeyTerm(String keyTerm) {
        this.keyTerm = keyTerm;
    }

    public String getPaperAbstract() {
        return paperAbstract;
    }

    public void setPaperAbstract(String paperAbstract) {
        this.paperAbstract = paperAbstract;
    }

    public NaucnaOblast getScienceField() {
        return scienceField;
    }

    public void setScienceField(NaucnaOblast scienceField) {
        this.scienceField = scienceField;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public byte[] getPdf() {
        return pdf;
    }

    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }

    public List<Koautor> getKoautori() {
        return koautori;
    }

    public void setKoautori(List<Koautor> koautori) {
        this.koautori = koautori;
    }

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }
}
