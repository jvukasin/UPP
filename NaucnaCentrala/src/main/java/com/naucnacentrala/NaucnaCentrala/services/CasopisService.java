package com.naucnacentrala.NaucnaCentrala.services;

import com.naucnacentrala.NaucnaCentrala.model.Casopis;
import com.naucnacentrala.NaucnaCentrala.model.User;
import com.naucnacentrala.NaucnaCentrala.repository.CasopisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CasopisService {

    @Autowired
    CasopisRepository casopisRepository;

    public List<Casopis> findAll() {
        return casopisRepository.findAll();
    }

    public Casopis findOneByUsername(Long id) {
        return casopisRepository.findOneById(id);
    }

    public Casopis save(Casopis casopis) {
        return casopisRepository.save(casopis);
    }

    public void remove(String username) {
        casopisRepository.deleteById(username);
    }
}
