
package com.te.testyantrabank.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.te.testyantrabank.dao.AdminDao;
import com.te.testyantrabank.dao.CustomerDao;
import com.te.testyantrabank.dto.Admin;
import com.te.testyantrabank.dto.Customer;

@SpringBootTest
class AdminServiceImplTest {
	@InjectMocks
	AdminServiceImpl adminserviceImpl;
	@Mock
	private AdminDao adminDao;
	@Mock
	private CustomerDao customerDao;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
	}

	@SuppressWarnings("unchecked")
	@Test
	void testGetAllCustomer() {
		Admin admin = new Admin();
		admin.setUserName("Cherry");
		admin.setPassword("admin8861");
		when(customerDao.findAll()).thenReturn(Stream
				.of(new Customer("Shivaa", 868985, "qwerty", 5000, 0), new Customer("Radhe", 868986, "qwerty", 5000, 0))
				.collect(Collectors.toList()));
		List<Customer> customers = (List<Customer>) adminserviceImpl.getAllCustomer().getData();
		assertEquals(2, customers.size());
	}

}
