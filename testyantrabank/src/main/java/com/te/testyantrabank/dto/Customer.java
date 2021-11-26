package com.te.testyantrabank.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "cid", "password", "balance", "count" })
@Table(name = "customer_details", uniqueConstraints = @UniqueConstraint(columnNames = { "userName", "accountNo" }))
public class Customer implements Serializable {
	@Id
	@SequenceGenerator(name = "customerSequence", initialValue = 100, allocationSize = 1)
	@GeneratedValue(generator = "customerSequence")
	private int cid;
	private String userName;
	private long accountNo;
	private String password;
	private double balance;
	private int count;

	@OneToMany(mappedBy = "customer")
	private List<BalanceDetails> balanceDetails;

	public Customer(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	public Customer(String userName, long accountNo, String password, double balance, int count) {
		this.userName = userName;
		this.accountNo = accountNo;
		this.password = password;
		this.balance = balance;
		this.count = count;
	}

	@Override
	public String toString() {
		return "Customer [cid=" + cid + ", userName=" + userName + ", accountNo=" + accountNo + ", password=" + password
				+ ", balance=" + balance + ", count=" + count + "]";
	}

}
