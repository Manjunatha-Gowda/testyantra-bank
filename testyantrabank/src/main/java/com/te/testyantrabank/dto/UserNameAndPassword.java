package com.te.testyantrabank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNameAndPassword {
	
	private String userName;
	private String password;
	private double amount;
	
	public UserNameAndPassword(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
}
