package com.naucnacentrala.NaucnaCentrala.services.tasks;

import com.naucnacentrala.NaucnaCentrala.model.*;
import com.naucnacentrala.NaucnaCentrala.services.NaucniRadService;
import com.naucnacentrala.NaucnaCentrala.services.elasticSearch.ReviewerESService;
import com.naucnacentrala.NaucnaCentrala.services.elasticSearch.SciencePaperESService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class AddESDOI implements JavaDelegate {

    @Autowired
    private NaucniRadService naucniRadService;

    @Autowired
    private SciencePaperESService sciencePaperESService;

    @Autowired
    private ReviewerESService reviewerESService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        NaucniRad naucniRad = naucniRadService.findOneById((Long) delegateExecution.getVariable("radID"));
        List<Koautor> coauthorList = new ArrayList<>();
        for(Koautor koautor: naucniRad.getKoautori()){
            coauthorList.add(koautor);
        }

        // sacuvaj u elastic-u
        SciencePaperES sciencePaperES = new SciencePaperES();
        String text = sciencePaperESService.parsePDF(naucniRad);
        sciencePaperES.setText(text);
        sciencePaperES.setId(naucniRad.getId().toString());
        sciencePaperES.setKeyTerms(naucniRad.getKeyTerm());
        sciencePaperES.setTitle(naucniRad.getTitle());
        sciencePaperES.setPaperAbstract(naucniRad.getPaperAbstract());
        sciencePaperES.setScienceField(naucniRad.getScienceField().getNaziv());
        sciencePaperES.setMagazineName(naucniRad.getMagazine().getNaziv());
        sciencePaperES.setFilePath(naucniRadService.getPath(naucniRad.getId()));
        Random rand = new Random();
        int prefix = rand.nextInt(1000) + 1;
        int suffix = rand.nextInt(1000) + 1;
        sciencePaperES.setDoi("10." + prefix + "/" + suffix);

        sciencePaperES = sciencePaperESService.save(sciencePaperES);

        // sacuvaj recenzente
        Casopis casopis = naucniRad.getMagazine();
        for(Recenzent recen: casopis.getRecenzenti()) {
            ReviewerES reviewerES = new ReviewerES();
            reviewerES.setScienceFields(recen.getNaucneOblasti());
            reviewerES.setFirstName(recen.getIme());
            reviewerES.setLastName(recen.getPrezime());
            reviewerES.setEmail(recen.getEmail());
            reviewerES.setId(recen.getUsername());
            reviewerES.getSciencePapers().add(sciencePaperES);
            reviewerES.setLocation(null);
            reviewerESService.save(reviewerES);
        }
    }
}
