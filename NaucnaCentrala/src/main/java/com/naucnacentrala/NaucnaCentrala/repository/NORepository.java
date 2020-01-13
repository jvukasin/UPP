package com.naucnacentrala.NaucnaCentrala.repository;

import com.naucnacentrala.NaucnaCentrala.model.NaucnaOblast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NORepository extends JpaRepository<NaucnaOblast, String>
{
    List<NaucnaOblast> findAll();

    NaucnaOblast findOneBySifra(Long sifra);

    void deleteBySifra(Long sifra);
}