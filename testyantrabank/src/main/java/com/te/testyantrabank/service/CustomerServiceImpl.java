package com.te.testyantrabank.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.te.testyantrabank.dao.AdminDao;
import com.te.testyantrabank.dao.BalanceDetailsDao;
import com.te.testyantrabank.dao.CustomerDao;
import com.te.testyantrabank.dto.Admin;
import com.te.testyantrabank.dto.BalanceDetails;
import com.te.testyantrabank.dto.Customer;
import com.te.testyantrabank.exception.CustomerException;
import com.te.testyantrabank.message.Message;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService, UserDetailsService {
	@Autowired
	private CustomerDao customerDao;
	@Autowired
	private BalanceDetailsDao balanceDetails;
	@Autowired
	private AdminDao adminDao;
	private static Customer customer;

	@SuppressWarnings("static-access")
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		if (!username.equals("Cherry")) {
			this.customer = customerDao.findByUserName(username);
			if (customer == null) {
				log.error("Customer UserName Not Present In Databse");
				throw new CustomerException("Please Enter your Correct Customer User Name");
			}
			authorities.add(new SimpleGrantedAuthority("USER"));
			return new User(customer.getUserName(), customer.getPassword(), authorities);
		} else {
			Admin admin = adminDao.findByUserName(username);
			if (admin == null) {
				log.error("Admin UserName Not Present In Databse");
				throw new CustomerException("Please Enter your Correct Admin User Name");
			}
			authorities.add(new SimpleGrantedAuthority("ADMIN"));
			return new User(admin.getUserName(), admin.getPassword(), authorities);
		}
	}

	@Value("${deposit.fees}")
	double depositfees;

	@Override
	public Message deposit(double amount) {
		double avialablewAmount = (double) Math.round((customer.getBalance() + amount * (1 - depositfees)) * 1000.0)
				/ 1000.0;
		if (amount % 100 == 0 && amount > 0) {
			customer.setBalance(avialablewAmount);
			this.customerDao.save(customer);
			this.balanceDetails.save(new BalanceDetails(0, amount, new Date(), avialablewAmount, customer));
			System.out.println(this.customerDao);
			Customer customer2 = (Customer) this.customerDao.findByUserName(customer.getUserName());
			return new Message(HttpStatus.OK.value(), new Date(), false, amount + " Amount Successfully Deposited  ",
					customer2);
		}
		log.error("The Amount Should be Multiple's of 100 Only");
		throw new CustomerException("The Amount Should be Multiple's of 100 Only");
	}

	@Value("${withdraw.fees}")
	double withdrawfees;

	@Override
	public Message withdraw(double Withdrawamount) {
		double avialablewAmount = (double) Math
				.round((customer.getBalance() - Withdrawamount * (1 + withdrawfees)) * 1000.0) / 1000.0;
		if (Withdrawamount % 100 != 0) {
			log.error("The Amount Should be Multiple's of 100 only");
			throw new CustomerException("The Amount Should be Multiple's of 100 only");
		}
		if (avialablewAmount > 0 && customer.getBalance() > 500) {
			if (customer.getCount() < 3) {
				customer.setBalance(avialablewAmount);
				customer.setCount(customer.getCount() + 1);
				customerDao.save(customer);
				balanceDetails.save(new BalanceDetails(Withdrawamount, 0, new Date(), avialablewAmount, customer));
				Customer customer2 = (Customer) customerDao.findByUserName(customer.getUserName());
				return new Message(HttpStatus.OK.value(), new Date(), false,
						Withdrawamount + " Rupees Withdrawn Successfully  ", customer2);
			}
			log.error("  Withdrawl's Allowed Only 3 Times In a Month  ");
			throw new CustomerException("  Withdrawl's Allowed Only 3 Times In a Month  ");
		}
		log.error(" Insufficient Balance In Your Account......!!! ");
		throw new CustomerException(" Insufficient Balance In Your Account......!!! ");
	}

	@Override
	public Message getBalance() {
		if (customer == null || customer.getUserName() == null) {
			throw new CustomerException(" Login First......!!!");
		}
		Customer customer2 = (Customer) customerDao.findByUserName(customer.getUserName());
		return new Message(HttpStatus.OK.value(), new Date(), false,
				"Available Balance In Your Account is  : " + customer2.getBalance(), customer2);
	}

	@Override
	public Customer findByUserName(String userName) {
		customer = (Customer) customerDao.findByUserName(userName);
		log.info("Successfully Logged In " + userName);
		return customer;
	}

	public Customer getCustomer() {
		return customer;
	}

}
