package com.naucnacentrala.NaucnaCentrala.services;

import com.naucnacentrala.NaucnaCentrala.model.Clanarina;
import com.naucnacentrala.NaucnaCentrala.repository.jpa.ClanarinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClanarinaService {

    @Autowired
    ClanarinaRepository clanarinaRepository;

    public Clanarina findOneById(Long id) {
        return clanarinaRepository.findOneById(id);
    }

    public List<Clanarina> findAll() {
        return clanarinaRepository.findAll();
    }

    //@Transactional
    public Clanarina save (Clanarina c) {
        return clanarinaRepository.save(c);
    }

    @Transactional
    public void remove(Long id) {
        clanarinaRepository.deleteById(id);
    }
}
