package com.naucnacentrala.NaucnaCentrala.model;

import org.springframework.data.elasticsearch.annotations.Document;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

import java.util.ArrayList;
import java.util.List;

@Document(indexName = "reviewers", type = "reviewer", shards = 1, replicas = 0)
public class ReviewerES {

    @Id
    @Field(type = FieldType.Text, index = false, store = true)
    private String id;

    @Field(type = FieldType.Text, store = true)
    private String firstName;

    @Field(type = FieldType.Text, store = true)
    private String lastName;

    @Field(type = FieldType.Text, store = true)
    private String email;

    @GeoPointField
    private GeoPoint location;

    @Field(type = FieldType.Text, store = true)
    private List<String> scienceFields = new ArrayList<>();

    @Field(type = FieldType.Nested, store = true)
    private List<SciencePaperES> sciencePapers = new ArrayList<>();

    public ReviewerES(String id, String firstName, String lastName, String email, GeoPoint location, List<String> scienceFields, List<SciencePaperES> sciencePapers) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.location = location;
        this.scienceFields = scienceFields;
        this.sciencePapers = sciencePapers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public List<String> getScienceFields() {
        return scienceFields;
    }

    public void setScienceFields(List<String> scienceFields) {
        this.scienceFields = scienceFields;
    }

    public List<SciencePaperES> getSciencePapers() {
        return sciencePapers;
    }

    public void setSciencePapers(List<SciencePaperES> sciencePapers) {
        this.sciencePapers = sciencePapers;
    }
}
