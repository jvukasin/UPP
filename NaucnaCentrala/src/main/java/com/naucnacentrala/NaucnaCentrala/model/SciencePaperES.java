package com.naucnacentrala.NaucnaCentrala.model;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;

@Document(indexName = "magazine", type = "sciencePaper", shards = 1, replicas = 0)
public class SciencePaperES {

    @Id
    @Field(type = FieldType.Text, index = false, store = true)
    private String id;

    @Field(type = FieldType.Text, store = true)
    private String doi;

    @Field(type = FieldType.Text, store = true)
    private String title;

    @Field(type = FieldType.Text, store = true)
    private String magazineName;

    @Field(type = FieldType.Text, store = true)
    private String keyTerms;

    @Field(type = FieldType.Text, store = true)
    private String paperAbstract;

    @Field(type = FieldType.Text, store = true)
    private String scienceField;

    @Field(type = FieldType.Text, store = true)
    private String text;

    @Field(type = FieldType.Text, store = true)
    private String filePath;

    @Field(type = FieldType.Text, store = true)
    private String author;

    public SciencePaperES() {
    }

    public SciencePaperES(String id, String doi, String title, String magazineName, String keyTerms, String paperAbstract, String scienceField, String text, String filePath, String author) {
        this.id = id;
        this.doi = doi;
        this.title = title;
        this.magazineName = magazineName;
        this.keyTerms = keyTerms;
        this.paperAbstract = paperAbstract;
        this.scienceField = scienceField;
        this.text = text;
        this.filePath = filePath;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMagazineName() {
        return magazineName;
    }

    public void setMagazineName(String magazineName) {
        this.magazineName = magazineName;
    }

    public String getKeyTerms() {
        return keyTerms;
    }

    public void setKeyTerms(String keyTerms) {
        this.keyTerms = keyTerms;
    }

    public String getPaperAbstract() {
        return paperAbstract;
    }

    public void setPaperAbstract(String paperAbstract) {
        this.paperAbstract = paperAbstract;
    }

    public String getScienceField() {
        return scienceField;
    }

    public void setScienceField(String scienceField) {
        this.scienceField = scienceField;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if(text == null){
            this.text = "";
        }
        this.text = text;
    }
}
