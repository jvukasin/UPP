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

    public CasopisDTO(Long id, String name, String issn, List<NaucnaOblast> scienceFieldList, Urednik chiefEditor,
                      boolean isRegistered, long sellerId, List<NaucniRad> sciencePapers, boolean active) {
        this.id = id;
        this.name = name;
        this.issn = issn;
        this.setScienceFieldList(scienceFieldList);
        this.setChiefEditor(chiefEditor);
        this.isRegistered = isRegistered;
        this.setSellerId(sellerId);
        this.setSciencePaperDTOList(sciencePapers);
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

    public void setScienceFieldList(List<NaucnaOblast> scienceFieldList) {
        for(NaucnaOblast scienceField: scienceFieldList){
            this.scienceFieldList.add(scienceField.getNaziv());
        }
    }
    public String getChiefEditor() {
        return chiefEditor;
    }

    public void setChiefEditor(Urednik chiefEditor) {
        this.chiefEditor = chiefEditor.getIme() + " " + chiefEditor.getPrezime();
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public void setSciencePaperDTOList(List<NaucniRad> sciencePapers) {
        for(NaucniRad paper: sciencePapers){
            this.sciencePaperDTOList.add(new NaucniRadDTO(paper.getId(), paper.getTitle(), paper.getKeyTerm(), paper.getPaperAbstract(), paper.getPrice(), paper.getCurrency()));
        }
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

}
