package com.naucnacentrala.NaucnaCentrala.repository;

import com.naucnacentrala.NaucnaCentrala.model.Clanarina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClanarinaRepository extends JpaRepository<Clanarina, String> {

    @Query("select clan from Clanarina clan where clan.casopis = :casopis and clan.agreementID = :agrID")
    Clanarina findByAgrAndCas(long casopis, long agrID);
}
