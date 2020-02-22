package com.naucnacentrala.NaucnaCentrala.services;

import com.naucnacentrala.NaucnaCentrala.model.NaucniRad;
import com.naucnacentrala.NaucnaCentrala.repository.jpa.NaucniRadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class NaucniRadService {

    private final String path = "D:\\git\\UPP_projekat\\NaucnaCentrala\\src\\main\\resources\\files";
    private final Path storageLocation = Paths.get(this.path);

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

    public String getPath(Long id) throws UnsupportedEncodingException {
        NaucniRad naucniRad = naucniRadRepository.findOneById(id);
        return this.path + "\\" + naucniRad.getPdfName();
    }


}
