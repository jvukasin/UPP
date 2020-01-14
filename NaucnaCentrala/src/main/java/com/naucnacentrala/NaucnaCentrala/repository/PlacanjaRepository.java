package com.naucnacentrala.NaucnaCentrala.repository;

import com.naucnacentrala.NaucnaCentrala.model.NacinPlacanja;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlacanjaRepository extends JpaRepository<NacinPlacanja, String>
{
    List<NacinPlacanja> findAll();

    NacinPlacanja findOneById(Long id);

    void deleteById(Long id);
}