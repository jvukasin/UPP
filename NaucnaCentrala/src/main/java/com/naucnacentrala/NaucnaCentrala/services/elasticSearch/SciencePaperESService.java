package com.naucnacentrala.NaucnaCentrala.services.elasticSearch;

import com.naucnacentrala.NaucnaCentrala.model.SciencePaperES;
import com.naucnacentrala.NaucnaCentrala.repository.elasticSearch.SciencePaperESRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SciencePaperESService {

    @Autowired
    private SciencePaperESRepository sciencePaperESRepo;


    public SciencePaperES save(SciencePaperES sciencePaperES){
        return sciencePaperESRepo.save(sciencePaperES);
    }


    public SciencePaperES findOneById(String id) {
        return sciencePaperESRepo.findOneById(id);
    }
}
