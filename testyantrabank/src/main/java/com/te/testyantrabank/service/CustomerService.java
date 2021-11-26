package com.te.testyantrabank.service;

import com.te.testyantrabank.dto.Customer;
import com.te.testyantrabank.message.Message;

public interface CustomerService {

	/*
	 * Message findByUserName(String userName, String password);
	 * 
	 * Message deposit(double amount, Customer customer);
	 * 
	 * Message withdraw(double amount, Customer customer);
	 * 
	 * Message getBalance(Customer customer);
	 */
	
	
	Customer findByUserName(String userName);

	Message deposit(double amount);

	Message withdraw(double amount);

	Message getBalance();

}
