package com.naucnacentrala.NaucnaCentrala.services;

import com.naucnacentrala.NaucnaCentrala.model.NaucniRad;
import com.naucnacentrala.NaucnaCentrala.repository.NaucniRadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NaurcniRadService {

    @Autowired
    NaucniRadRepository sciencePaperRepository;

    public NaucniRad findOneById(Long id){
        return sciencePaperRepository.findOneById(id);
    }
}
