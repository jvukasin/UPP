package com.naucnacentrala.NaucnaCentrala.repository;

import com.naucnacentrala.NaucnaCentrala.model.Casopis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CasopisRepository extends JpaRepository<Casopis, String> {

    List<Casopis> findAll();
    Casopis findOneById(Long id);
}
