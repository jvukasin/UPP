package com.naucnacentrala.NaucnaCentrala.repository;

import com.naucnacentrala.NaucnaCentrala.model.Casopis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CasopisRepository extends JpaRepository<Casopis, String> {

    List<Casopis> findAll();
    Casopis findOneById(Long id);

    Casopis findBySellerId(long sellerId);

    @Query("select magazine from Casopis magazine where magazine.glavniUrednik.username = :username")
    List<Casopis> finAllByUrednik(@Param("username") String username);
}
