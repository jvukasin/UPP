package com.naucnacentrala.NaucnaCentrala.services;

import com.naucnacentrala.NaucnaCentrala.dto.CasopisDTO;
import com.naucnacentrala.NaucnaCentrala.model.Casopis;
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
        CasopisDTO dto = new CasopisDTO();
        dto.setId(c.getId());
        dto.setName(c.getNaziv());
        dto.setIssn(c.getIssn());
        dto.setScienceFieldList(c.getNaucneOblasti());
        dto.setChiefEditor(c.getGlavniUrednik());
        dto.setRegistered(c.isRegistered());
        dto.setSellerId(c.getSellerId());
        dto.setActive(c.isAktivan());
        dto.setSciencePaperDTOList(c.getSciencePapers());
        return dto;
    }

    public List<CasopisDTO> findAllDto() {
        ArrayList<CasopisDTO> retVal = new ArrayList<>();
        List<Casopis> casopisi = casopisRepository.findAll();
        for(Casopis c : casopisi) {
            CasopisDTO dto = new CasopisDTO();
            dto.setId(c.getId());
            dto.setName(c.getNaziv());
            dto.setIssn(c.getIssn());
            dto.setScienceFieldList(c.getNaucneOblasti());
            dto.setChiefEditor(c.getGlavniUrednik());
            dto.setRegistered(c.isRegistered());
            dto.setSellerId(c.getSellerId());
            dto.setActive(c.isAktivan());
            dto.setSciencePaperDTOList(c.getSciencePapers());

            retVal.add(dto);
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
