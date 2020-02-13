package com.naucnacentrala.NaucnaCentrala.services;

import com.naucnacentrala.NaucnaCentrala.dto.CasopisDTO;
import com.naucnacentrala.NaucnaCentrala.dto.NaucniRadDTO;
import com.naucnacentrala.NaucnaCentrala.model.Casopis;
import com.naucnacentrala.NaucnaCentrala.model.NaucnaOblast;
import com.naucnacentrala.NaucnaCentrala.model.NaucniRad;
import com.naucnacentrala.NaucnaCentrala.repository.CasopisRepository;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.FormFieldValidationConstraint;
import org.camunda.bpm.engine.form.FormType;
import org.camunda.bpm.engine.impl.form.type.StringFormType;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CasopisService {

    @Autowired
    CasopisRepository casopisRepository;

    public List<Casopis> findAll() {
        return casopisRepository.findAll();
    }

    public Casopis findOneById(Long id) {
        return casopisRepository.findOneById(id);
    }

    public Casopis save(Casopis casopis) {
        return casopisRepository.save(casopis);
    }

    public void remove(String username) {
        casopisRepository.deleteById(username);
    }

    public Casopis findBySellerId(long sellerId ) { return casopisRepository.findBySellerId(sellerId); }

    public CasopisDTO findOneDto (long id) {
        Casopis c = casopisRepository.findOneById(id);
        List<String> no = new ArrayList<>();
        for(NaucnaOblast o : c.getNaucneOblasti()) {
            no.add(o.getNaziv());
        }
        String urednik = c.getGlavniUrednik().getIme() + " " + c.getGlavniUrednik().getPrezime();
        List<NaucniRadDTO> radovi = new ArrayList<>();
        for(NaucniRad r : c.getSciencePapers()) {
            NaucniRadDTO dto = new NaucniRadDTO(r.getId(), r.getTitle(), r.getKeyTerm(), r.getPaperAbstract(), r.getPrice(), r.getCurrency());
            radovi.add(dto);
        }
        CasopisDTO dto;
        if(c.getSellerId() != null) {
            dto = new CasopisDTO(c.getId(), c.getNaziv(), c.getIssn(), no, urednik, c.isRegistered(), c.getSellerId(), radovi, c.isAktivan());
        } else {
            dto = new CasopisDTO(c.getId(), c.getNaziv(), c.getIssn(), no, urednik, c.isRegistered(), new Long(0), radovi, c.isAktivan());
        }
        return dto;
    }

    public List<CasopisDTO> findAllByUrednik(String korisnik) {
        List<CasopisDTO> casopisi = new ArrayList<>();
        for(Casopis c : casopisRepository.finAllByUrednik(korisnik)) {
            CasopisDTO dto;
            List<String> no = new ArrayList<>();
            for(NaucnaOblast o : c.getNaucneOblasti()) {
                no.add(o.getNaziv());
            }
            String urednik = c.getGlavniUrednik().getIme() + " " + c.getGlavniUrednik().getPrezime();
            List<NaucniRadDTO> radovi = new ArrayList<>();
            for(NaucniRad r : c.getSciencePapers()) {
                NaucniRadDTO nr = new NaucniRadDTO(r.getId(), r.getTitle(), r.getKeyTerm(), r.getPaperAbstract(), r.getPrice(), r.getCurrency());
                radovi.add(nr);
            }
            if(c.getSellerId() != null) {
                dto = new CasopisDTO(c.getId(), c.getNaziv(), c.getIssn(), no, urednik, c.isRegistered(), c.getSellerId(), radovi, c.isAktivan());
            } else {
                dto = new CasopisDTO(c.getId(), c.getNaziv(), c.getIssn(), no, urednik, c.isRegistered(), new Long(0), radovi, c.isAktivan());
            }
            casopisi.add(dto);
        }
        return casopisi;
    }

    public List<CasopisDTO> findAllDto() {
        ArrayList<CasopisDTO> retVal = new ArrayList<>();
        List<Casopis> casopisi = casopisRepository.findAll();
        for(Casopis c : casopisi) {
            List<String> no = new ArrayList<>();
            for(NaucnaOblast o : c.getNaucneOblasti()) {
                no.add(o.getNaziv());
            }
            String urednik = c.getGlavniUrednik().getIme() + " " + c.getGlavniUrednik().getPrezime();
            List<NaucniRadDTO> radovi = new ArrayList<>();
            for(NaucniRad r : c.getSciencePapers()) {
                NaucniRadDTO nr = new NaucniRadDTO(r.getId(), r.getTitle(), r.getKeyTerm(), r.getPaperAbstract(), r.getPrice(), r.getCurrency());
                radovi.add(nr);
            }
            CasopisDTO dto;
            if(c.getSellerId() != null) {
                dto = new CasopisDTO(c.getId(), c.getNaziv(), c.getIssn(), no, urednik, c.isRegistered(), c.getSellerId(), radovi, c.isAktivan());
            } else {
                dto = new CasopisDTO(c.getId(), c.getNaziv(), c.getIssn(), no, urednik, c.isRegistered(), new Long(0), radovi, c.isAktivan());
            }retVal.add(dto);
        }
        return retVal;
    }

//    HELPERS

    public FormField napraviUrStringPolje(String val) {
        FormField field = new FormField() {
            @Override
            public String getId() {
                return "prikaz_urednika";
            }

            @Override
            public String getLabel() {
                return "Urednici";
            }

            @Override
            public FormType getType() {
                return new StringFormType();
            }

            @Override
            public String getTypeName() {
                return "string";
            }

            @Override
            public Object getDefaultValue() {
                return val;
            }

            @Override
            public TypedValue getValue() {
                return null;
            }

            @Override
            public List<FormFieldValidationConstraint> getValidationConstraints() {
                return null;
            }

            @Override
            public Map<String, String> getProperties() {
                return null;
            }

            @Override
            public boolean isBusinessKey() {
                return false;
            }
        };
        return field;
    }

    public FormField napraviRecStringPolje(String val) {
        FormField field = new FormField() {
            @Override
            public String getId() {
                return "prikaz_recenzenata";
            }

            @Override
            public String getLabel() {
                return "Recenzenti";
            }

            @Override
            public FormType getType() {
                return new StringFormType();
            }

            @Override
            public String getTypeName() {
                return "string";
            }

            @Override
            public Object getDefaultValue() {
                return val;
            }

            @Override
            public TypedValue getValue() {
                return null;
            }

            @Override
            public List<FormFieldValidationConstraint> getValidationConstraints() {
                return null;
            }

            @Override
            public Map<String, String> getProperties() {
                return null;
            }

            @Override
            public boolean isBusinessKey() {
                return false;
            }
        };
        return field;
    }

    public FormField napraviNaucneStringPolje(String val) {
        FormField field = new FormField() {
            @Override
            public String getId() {
                return "prikaz_naucnih";
            }

            @Override
            public String getLabel() {
                return "Naučne oblasti";
            }

            @Override
            public FormType getType() {
                return new StringFormType();
            }

            @Override
            public String getTypeName() {
                return "string";
            }

            @Override
            public Object getDefaultValue() {
                return val;
            }

            @Override
            public TypedValue getValue() {
                return null;
            }

            @Override
            public List<FormFieldValidationConstraint> getValidationConstraints() {
                return null;
            }

            @Override
            public Map<String, String> getProperties() {
                return null;
            }

            @Override
            public boolean isBusinessKey() {
                return false;
            }
        };
        return field;
    }

    public FormField prethodniRec(String val) {
        FormField field = new FormField() {
            @Override
            public String getId() {
                return "pretRec";
            }

            @Override
            public String getLabel() {
                return "Prethodno izabrani recenzenti";
            }

            @Override
            public FormType getType() {
                return new StringFormType();
            }

            @Override
            public String getTypeName() {
                return "string";
            }

            @Override
            public Object getDefaultValue() {
                return val;
            }

            @Override
            public TypedValue getValue() {
                return null;
            }

            @Override
            public List<FormFieldValidationConstraint> getValidationConstraints() {
                return null;
            }

            @Override
            public Map<String, String> getProperties() {
                return null;
            }

            @Override
            public boolean isBusinessKey() {
                return false;
            }
        };
        return field;
    }

    public FormField prethodniUred(String val) {
        FormField field = new FormField() {
            @Override
            public String getId() {
                return "pretUr";
            }

            @Override
            public String getLabel() {
                return "Prethodno izabrani urednici";
            }

            @Override
            public FormType getType() {
                return new StringFormType();
            }

            @Override
            public String getTypeName() {
                return "string";
            }

            @Override
            public Object getDefaultValue() {
                return val;
            }

            @Override
            public TypedValue getValue() {
                return null;
            }

            @Override
            public List<FormFieldValidationConstraint> getValidationConstraints() {
                return null;
            }

            @Override
            public Map<String, String> getProperties() {
                return null;
            }

            @Override
            public boolean isBusinessKey() {
                return false;
            }
        };
        return field;
    }
}
