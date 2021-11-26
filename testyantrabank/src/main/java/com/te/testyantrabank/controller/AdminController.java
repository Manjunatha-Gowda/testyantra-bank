package com.te.testyantrabank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.testyantrabank.message.Message;
import com.te.testyantrabank.service.AdminService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/api/v1/admin")
@Api(value = "Admin", tags = { "Admin Operations" })
public class AdminController {
	@Autowired
	private AdminService adminService;

	@GetMapping(path = "/customers")
	@ApiOperation(value = "View All Customers",notes = "View All Customers",tags="TestYantra Bank")
	public ResponseEntity<Message> getAllCustomer() {
		return new ResponseEntity<Message>(adminService.getAllCustomer(), HttpStatus.OK);
	}
}
