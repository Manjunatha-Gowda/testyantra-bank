package com.te.testyantrabank.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.te.testyantrabank.dao.CustomerDao;
import com.te.testyantrabank.message.Message;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private CustomerDao customerDao;

	@Override
	public Message getAllCustomer() {
		log.info(" Sucessfully fetched All the Customers  ");
		return new Message(HttpStatus.OK.value(), new Date(), false, " Sucessfully fetched All the Customers  ",
				customerDao.findAll());
	}

}
