package com.te.testyantrabank.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.testyantrabank.dto.BalanceDetails;

@Repository
public interface BalanceDetailsDao extends JpaRepository<BalanceDetails, Integer> {

}
