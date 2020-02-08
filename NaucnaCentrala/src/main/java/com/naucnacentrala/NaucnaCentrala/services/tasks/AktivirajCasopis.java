package com.naucnacentrala.NaucnaCentrala.services.tasks;

import com.naucnacentrala.NaucnaCentrala.model.Casopis;
import com.naucnacentrala.NaucnaCentrala.services.CasopisService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AktivirajCasopis implements JavaDelegate {

    @Autowired
    CasopisService casopisService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long casopisID = (Long) execution.getVariable("casopisID");
        Casopis casopis = casopisService.findOneById(casopisID);
        casopis.setAktivan(true);
        casopisService.save(casopis);
    }
}
