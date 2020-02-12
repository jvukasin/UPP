package com.naucnacentrala.NaucnaCentrala.repository;

import com.naucnacentrala.NaucnaCentrala.model.OrderObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderObjectRepository extends JpaRepository<OrderObject, Long> {

    @Query("select ord from OrderObject ord where ord.userId = :username")
    List<OrderObject> findAllByUser(String username);
}
