package com.naucnacentrala.NaucnaCentrala.dto;


import com.naucnacentrala.NaucnaCentrala.model.NaucnaOblast;
import com.naucnacentrala.NaucnaCentrala.model.NaucniRad;
import com.naucnacentrala.NaucnaCentrala.model.Urednik;

import java.util.ArrayList;
import java.util.List;

public class CasopisDTO {

    private Long id;
    private String name;
    private String issn;
    private boolean isRegistered;
    private Long sellerId;
    private List<String> scienceFieldList = new ArrayList<>();
    private String chiefEditor;
    private List<NaucniRadDTO> sciencePaperDTOList = new ArrayList<>();
    private boolean active;

    public CasopisDTO() {
    }

    public CasopisDTO(Long id, String name, String issn, List<String> scienceFieldList, String chiefEditor,
                      boolean isRegistered, long sellerId, List<NaucniRadDTO> sciencePapers, boolean active) {
        this.id = id;
        this.name = name;
        this.issn = issn;
        this.scienceFieldList = scienceFieldList;
        this.chiefEditor = chiefEditor;
        this.isRegistered = isRegistered;
        this.setSellerId(sellerId);
        this.sciencePaperDTOList = sciencePapers;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public List<String> getScienceFieldList() {
        return scienceFieldList;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public List<NaucniRadDTO> getSciencePaperDTOList() {
        return sciencePaperDTOList;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setScienceFieldList(List<String> scienceFieldList) {
        this.scienceFieldList = scienceFieldList;
    }

    public String getChiefEditor() {
        return chiefEditor;
    }

    public void setChiefEditor(String chiefEditor) {
        this.chiefEditor = chiefEditor;
    }

    public void setSciencePaperDTOList(List<NaucniRadDTO> sciencePaperDTOList) {
        this.sciencePaperDTOList = sciencePaperDTOList;
    }
}
