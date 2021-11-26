package com.te.testyantrabank.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.testyantrabank.dto.Admin;

@Repository
public interface AdminDao extends JpaRepository<Admin, Integer> {
	Admin findByUserName(String userName);
}
