package com.te.testyantrabank.dto;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@Entity
@NoArgsConstructor
@JsonIgnoreProperties({ "aid" })
public class Admin implements Serializable {
	@Id
	@SequenceGenerator(name = "adminSequence", initialValue = 100, allocationSize = 1)
	@GeneratedValue(generator = "adminSequence")
	private int aid;
	private String userName;
	private String password;

	public Admin(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

}
