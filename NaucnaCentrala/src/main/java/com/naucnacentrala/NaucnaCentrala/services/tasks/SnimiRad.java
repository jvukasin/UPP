package com.naucnacentrala.NaucnaCentrala.services.tasks;

import com.naucnacentrala.NaucnaCentrala.model.Casopis;
import com.naucnacentrala.NaucnaCentrala.model.NaucnaOblast;
import com.naucnacentrala.NaucnaCentrala.model.NaucniRad;
import com.naucnacentrala.NaucnaCentrala.services.CasopisService;
import com.naucnacentrala.NaucnaCentrala.services.NOService;
import com.naucnacentrala.NaucnaCentrala.services.NaucniRadService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SnimiRad implements JavaDelegate {

    @Autowired
    NaucniRadService naucniRadService;

    @Autowired
    CasopisService casopisService;

    @Autowired
    NOService noService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        Long radId = (Long) execution.getVariable("radID");
        NaucniRad rad = naucniRadService.findOneById(radId);

        Long casopisID = (Long) execution.getVariable("izabraniCasopisID");
        Casopis c = casopisService.findOneById(casopisID);
        String sifraNO = (String) execution.getVariable("konacan_nauc_obl");
        NaucnaOblast no = noService.findOneByUsername(Long.parseLong(sifraNO));

        String apstrakt = (String) execution.getVariable("konacan_apstrakt");
        String kljuc = (String) execution.getVariable("konacan_kljuc_poj");
        String valuta = (String) execution.getVariable("valuta");
        String cena = (String) execution.getVariable("cena");
        String naslov = (String) execution.getVariable("konacan_naslov");
        double pravaCena = Double.parseDouble(cena);

        rad.setMagazine(c);
        rad.setScienceField(no);
        rad.setCurrency(valuta);
        rad.setPrice(pravaCena);
        rad.setTitle(naslov);
        rad.setPaperAbstract(apstrakt);
        rad.setKeyTerm(kljuc);

        naucniRadService.save(rad);
    }
}
