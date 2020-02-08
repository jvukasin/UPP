package com.naucnacentrala.NaucnaCentrala.repository;

import com.naucnacentrala.NaucnaCentrala.model.NaucniRad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NaucniRadRepository extends JpaRepository<NaucniRad, Long> {

    NaucniRad findOneById(long id);
}