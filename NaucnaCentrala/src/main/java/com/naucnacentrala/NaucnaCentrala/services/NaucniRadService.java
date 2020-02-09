package com.naucnacentrala.NaucnaCentrala.services;

import com.naucnacentrala.NaucnaCentrala.model.NaucniRad;
import com.naucnacentrala.NaucnaCentrala.repository.NaucniRadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class NaucniRadService {

    @Autowired
    NaucniRadRepository naucniRadRepository;

    public NaucniRad findOneById(Long id){
        return naucniRadRepository.findOneById(id);
    }
    public NaucniRad save(NaucniRad r) {
        return naucniRadRepository.save(r);
    }
    public void delete(NaucniRad r) { naucniRadRepository.delete(r); }

    public NaucniRad savePdf(MultipartFile file, NaucniRad rad){
        try {
            rad.setPdf(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return naucniRadRepository.save(rad);
    }


}
