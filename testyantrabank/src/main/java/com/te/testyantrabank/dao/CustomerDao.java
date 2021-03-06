package com.te.testyantrabank.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.te.testyantrabank.dto.Customer;

@Repository
public interface CustomerDao extends JpaRepository<Customer, Integer> {
	Customer findByUserName(String userName);
	List<Customer> findAll();
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query(value = "update Customer c set c.balance = :balance,c.count = :count where c.user_name = :userName" , nativeQuery = true)
	boolean deposite(@Param("balance") double balance,@Param("count") int count,@Param("userName") String userName);
}
