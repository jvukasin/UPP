package com.naucnacentrala.NaucnaCentrala.services.tasks;

import com.naucnacentrala.NaucnaCentrala.CoordsFromAdressUtil;
import com.naucnacentrala.NaucnaCentrala.model.NaucniRad;
import com.naucnacentrala.NaucnaCentrala.model.Recenzent;
import com.naucnacentrala.NaucnaCentrala.model.ReviewerES;
import com.naucnacentrala.NaucnaCentrala.model.User;
import com.naucnacentrala.NaucnaCentrala.services.KorisnikService;
import com.naucnacentrala.NaucnaCentrala.services.elasticSearch.ReviewerESService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromeniURecenzenta implements JavaDelegate {

    @Autowired
    KorisnikService korisnikService;

    @Autowired
    IdentityService identityService;

    @Autowired
    ReviewerESService reviewerESService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String korisnik = (String) execution.getVariable("korisnik");
        identityService.createMembership(korisnik, "recenzenti");

        User k = korisnikService.findOneByUsername(korisnik);
        Recenzent r = new Recenzent(k);
        korisnikService.remove(k.getUsername());
        r = (Recenzent) korisnikService.save(r);

        ReviewerES reviewerES = new ReviewerES();
        reviewerES.setScienceFields(r.getNaucneOblasti());
        reviewerES.setFirstName(r.getIme());
        reviewerES.setLastName(r.getPrezime());
        reviewerES.setEmail(r.getEmail());
        reviewerES.setId(r.getUsername());
        CoordsFromAdressUtil coords = new CoordsFromAdressUtil(r.getGrad());
        reviewerES.setLocation(new GeoPoint(coords.getLat(), coords.getLon()));
        List<NaucniRad> radovi = r.getNaucniRadovi();
        String njegoviRadovi = "";
        njegoviRadovi += radovi.get(0).getTitle();
        for(int i=1; i< radovi.size(); i++) {
            njegoviRadovi += ", " + radovi.get(i).getTitle();
        }
        reviewerES.setSciencePapers(njegoviRadovi);
        reviewerESService.save(reviewerES);
    }
}
