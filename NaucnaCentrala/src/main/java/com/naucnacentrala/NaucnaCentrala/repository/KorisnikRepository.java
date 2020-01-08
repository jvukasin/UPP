package com.naucnacentrala.NaucnaCentrala.repository;

import com.naucnacentrala.NaucnaCentrala.model.Korisnik;
import com.naucnacentrala.NaucnaCentrala.model.NaucnaOblast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KorisnikRepository extends JpaRepository<Korisnik, String>
{
    List<Korisnik> findAll();
    Korisnik findOneByUsername(String username);

}