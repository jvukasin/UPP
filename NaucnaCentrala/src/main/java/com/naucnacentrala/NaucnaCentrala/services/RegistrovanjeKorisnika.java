package com.naucnacentrala.NaucnaCentrala.services;

import com.naucnacentrala.NaucnaCentrala.model.Korisnik;
import org.springframework.stereotype.Service;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import com.naucnacentrala.NaucnaCentrala.dto.FormSubmissionDTO;

import java.util.List;

@Service
public class RegistrovanjeKorisnika implements JavaDelegate {

    @Autowired
    IdentityService identityService;

    @Autowired
    KorisnikService korServis;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<FormSubmissionDTO> registration = (List<FormSubmissionDTO>)execution.getVariable("registration");
        User user = identityService.newUser("t");
        for (FormSubmissionDTO formField : registration) {
            if(formField.getFieldId().equals("username")) {
                user.setId(formField.getFieldValue());
            }
            if(formField.getFieldId().equals("password")) {
                user.setPassword(formField.getFieldValue());
            }
            if(formField.getFieldId().equals("email")) {
                user.setEmail(formField.getFieldValue());
            }
            if(formField.getFieldId().equals("ime")) {
                user.setFirstName(formField.getFieldValue());
            }
            if(formField.getFieldId().equals("prezime")) {
                user.setLastName(formField.getFieldValue());
            }
        }
        identityService.saveUser(user);

        Korisnik k = korServis.findOneByUsername(user.getId());
        k.setAktivan(true);
        k = korServis.save(k);
    }
}