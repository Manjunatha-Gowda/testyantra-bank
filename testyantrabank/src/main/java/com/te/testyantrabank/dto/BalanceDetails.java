package com.te.testyantrabank.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Data
@NoArgsConstructor
@JsonIgnoreProperties({ "bid", "customer" })
public class BalanceDetails implements Serializable {

	@Id
	@SequenceGenerator(name = "sequenceBalanceDetails", initialValue = 100, allocationSize = 100)
	@GeneratedValue(generator = "sequenceBalanceDetails")
	private int bid;
	private double debit;
	private double credit;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date date;
	private double balance;
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	public BalanceDetails(double debit, double credit, Date date, double balance, Customer customer) {
		this.debit = debit;
		this.credit = credit;
		this.date = date;
		this.balance = balance;
		this.customer = customer;
	}

	@Override
	public String toString() {
		return "BalanceDetails [bid=" + bid + ", debit=" + debit + ", credit=" + credit + ", date=" + date
				+ ", balance=" + balance + ", customer=" + customer + "]";
	}
}
