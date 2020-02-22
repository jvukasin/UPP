package com.naucnacentrala.NaucnaCentrala.services.tasks;

import com.naucnacentrala.NaucnaCentrala.model.Koautor;
import com.naucnacentrala.NaucnaCentrala.model.NaucniRad;
import com.naucnacentrala.NaucnaCentrala.model.SciencePaperES;
import com.naucnacentrala.NaucnaCentrala.services.NaucniRadService;
import com.naucnacentrala.NaucnaCentrala.services.elasticSearch.ReviewerESService;
import com.naucnacentrala.NaucnaCentrala.services.elasticSearch.SciencePaperESService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddESDOI implements JavaDelegate {


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        
    }
}
