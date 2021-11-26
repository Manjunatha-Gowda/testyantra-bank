
package com.te.testyantrabank.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.te.testyantrabank.dao.BalanceDetailsDao;
import com.te.testyantrabank.dao.CustomerDao;
import com.te.testyantrabank.dto.Customer;

@SpringBootTest
class CustomerServiceImplTest {
	@InjectMocks
	CustomerServiceImpl customerServiceImpl;
	@Mock
	private BalanceDetailsDao balanceDetailsDao;
	@Mock
	private CustomerDao customerDao;

	@BeforeEach
	void setUp() throws Exception {
		customerServiceImpl.findByUserName(Mockito.any());
	}

	@Test
	void testDeposit() {
		Customer customer = new Customer();
		customer.setUserName("Sam");
		customer.setPassword("qwerty");
		customer.setAccountNo(20082642386l);
		customer.setBalance(3000);
		customer.setCount(0);
		Mockito.when(customerDao.findByUserName(customer.getUserName())).thenReturn(customer);
		customerServiceImpl.findByUserName(customer.getUserName());
		Customer customer1 = (Customer) customerServiceImpl.deposit(1000).getData();
		assertNotNull(customer1);
		assertEquals(customer.getUserName(), customer1.getUserName());
	}

	@Test
	void testWithdraw() {
		Customer customer = new Customer();
		customer.setUserName("Radhe");
		customer.setPassword("qwerty");
		customer.setAccountNo(20082642386l);
		customer.setBalance(4000);
		customer.setCount(0);
		Mockito.when(customerDao.findByUserName(customer.getUserName())).thenReturn(customer);
		customerServiceImpl.findByUserName(customer.getUserName());
		Customer customer2 = (Customer) customerServiceImpl.withdraw(1000).getData();
		assertNotNull(customer2);
		assertEquals(customer.getUserName(), customer2.getUserName());
	}

	@Test
	void testGetBalance() {
		Customer customer = new Customer();
		customer.setUserName("Shivaa");
		customer.setPassword("shivu@123");
		customer.setAccountNo(20082642386l);
		customer.setBalance(4000);
		customer.setCount(0);
		Mockito.when(customerDao.findByUserName(customer.getUserName())).thenReturn(customer);
		customerServiceImpl.findByUserName(customer.getUserName());
		Customer customer2 = (Customer) customerServiceImpl.getBalance().getData();
		assertNotNull(customer2);
		assertEquals(customer.getUserName(), customer2.getUserName());
	}

}
