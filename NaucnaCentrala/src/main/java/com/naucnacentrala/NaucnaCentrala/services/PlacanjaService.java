package com.naucnacentrala.NaucnaCentrala.services;

import com.naucnacentrala.NaucnaCentrala.model.NacinPlacanja;
import com.naucnacentrala.NaucnaCentrala.repository.PlacanjaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PlacanjaService {
    @Autowired
    PlacanjaRepository repo;

    public List<NacinPlacanja> findAll() {
        return repo.findAll();
    }

    public NacinPlacanja findOneByUsername(Long sifra) {
        return repo.findOneById(sifra);
    }

    public NacinPlacanja save(NacinPlacanja no) {
        return repo.save(no);
    }

    public void remove(Long sifra) {
        repo.deleteById(sifra);
    }
}