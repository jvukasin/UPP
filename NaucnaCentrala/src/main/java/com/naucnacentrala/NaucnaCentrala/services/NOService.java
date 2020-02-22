package com.naucnacentrala.NaucnaCentrala.services;


import com.naucnacentrala.NaucnaCentrala.model.NaucnaOblast;
import com.naucnacentrala.NaucnaCentrala.repository.jpa.NORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NOService {

    @Autowired
    NORepository repo;

    public List<NaucnaOblast> findAll() {
        return repo.findAll();
    }

    public NaucnaOblast findOneByUsername(Long sifra) {
        return repo.findOneBySifra(sifra);
    }

    public NaucnaOblast save(NaucnaOblast no) {
        return repo.save(no);
    }

    public void remove(Long sifra) {
        repo.deleteBySifra(sifra);
    }

}
