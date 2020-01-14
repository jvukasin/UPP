package com.naucnacentrala.NaucnaCentrala.services;

import com.naucnacentrala.NaucnaCentrala.dto.FormSubmissionDTO;
import com.naucnacentrala.NaucnaCentrala.model.Casopis;
import com.naucnacentrala.NaucnaCentrala.model.NacinPlacanja;
import com.naucnacentrala.NaucnaCentrala.model.NaucnaOblast;
import com.naucnacentrala.NaucnaCentrala.model.Urednik;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class SnimiCasopis  implements JavaDelegate {

    @Autowired
    IdentityService identityService;

    @Autowired
    CasopisService casopisService;

    @Autowired
    NOService naucService;

    @Autowired
    PlacanjaService placanjaService;

    @Autowired
    KorisnikService korisnikService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<FormSubmissionDTO> casopisi = (List<FormSubmissionDTO>)execution.getVariable("casopis");
        Casopis casopis = new Casopis();
        ArrayList<NaucnaOblast> oblastiIzBaze = (ArrayList) naucService.findAll();
        ArrayList<NacinPlacanja> placanjaIzBaze = (ArrayList) placanjaService.findAll();
        ArrayList<NaucnaOblast> no = new ArrayList<>();
        ArrayList<NacinPlacanja> np = new ArrayList<>();

        for (FormSubmissionDTO formField : casopisi) {
            if(formField.getFieldId().equals("naziv")) {
                casopis.setNaziv(formField.getFieldValue());
            }
            if(formField.getFieldId().equals("issn")) {
                casopis.setIssn(formField.getFieldValue());
            }
            if(formField.getFieldId().equals("naplata_clanarine")) {
                casopis.setClanarina(formField.getFieldValue());
            }
            if (formField.getFieldId().equals("nauc_oblasti")) {
                for(NaucnaOblast n : oblastiIzBaze) {
                    if(n.getNaziv().equalsIgnoreCase(formField.getFieldValue())) {
                        no.add(n);
                        break;
                    }
                }
            }
            if (formField.getFieldId().equals("nacin_placanja")) {
                for(NacinPlacanja n : placanjaIzBaze) {
                    if(n.getNaziv().equalsIgnoreCase(formField.getFieldValue())) {
                        np.add(n);
                        break;
                    }
                }
            }
        }

        String usrname = (String) execution.getVariable("glavniUrednik");
        Urednik u = (Urednik) korisnikService.findOneByUsername(usrname);

        casopis.setAktivan(false);
        casopis.setNaucneOblasti(no);
        casopis.setNaciniPlacanja(np);
        casopis.setGlavniUrednik(u);
        casopis = casopisService.save(casopis);
    }
}
