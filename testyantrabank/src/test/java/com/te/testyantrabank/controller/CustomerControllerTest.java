
package com.te.testyantrabank.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.te.testyantrabank.dto.Customer;
import com.te.testyantrabank.dto.UserNameAndPassword;
import com.te.testyantrabank.message.Message;
import com.te.testyantrabank.service.CustomerService;

@SpringBootTest
class CustomerControllerTest {

	@InjectMocks
	private CustomerController customerController;

	@Mock
	private CustomerService customerService;
	private MockMvc mockMvc;
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
		this.objectMapper = new ObjectMapper();
	}

	@SuppressWarnings("unchecked")
	@Test
	void testWithdraw() throws UnsupportedEncodingException, Exception {
		UserNameAndPassword password = new UserNameAndPassword("Shivaa", "qwerty");
		Customer customer = new Customer(password.getUserName(), password.getPassword());
		Message message = new Message();
		message.setData(customer);
		Mockito.when(customerService.withdraw(Mockito.anyDouble())).thenReturn(message);
		String jsonObject = objectMapper.writeValueAsString(password);
		String result = mockMvc
				.perform(put("/api/v1/customer/withdraw/" + 1000).sessionAttr("customer", customer)
						.contentType(MediaType.APPLICATION_JSON).content(jsonObject))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
		Message message1 = objectMapper.readValue(result, Message.class);

		Map<String, String> map = (Map<String, String>) message1.getData();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			assertEquals(customer.getUserName(), entry.getValue());
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	void testDeposit() throws UnsupportedEncodingException, Exception {
		UserNameAndPassword password = new UserNameAndPassword("Shivaa", "qwerty");
		Customer customer = new Customer(password.getUserName(), password.getPassword());
		Message message = new Message();
		message.setData(customer);
		Mockito.when(customerService.deposit(Mockito.anyDouble())).thenReturn(message);
		String jsonObject = objectMapper.writeValueAsString(password);
		String result = mockMvc
				.perform(put("/api/v1/customer/deposit/" + 1000).sessionAttr("customer", customer)
						.contentType(MediaType.APPLICATION_JSON).content(jsonObject))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
		Message message1 = objectMapper.readValue(result, Message.class);

		Map<String, String> map = (Map<String, String>) message1.getData();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			assertEquals(customer.getUserName(), entry.getValue());
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	void testGetBalance() throws UnsupportedEncodingException, Exception {
		UserNameAndPassword password = new UserNameAndPassword("Shivaa", "qwerty");
		Customer customer = new Customer(password.getUserName(), password.getPassword());
		Message message1 = new Message();
		message1.setData(customer);
		Mockito.when(customerService.getBalance()).thenReturn(message1);
		String jsonObject = objectMapper.writeValueAsString(password);
		String result = mockMvc
				.perform(get("/api/v1/customer/balance").sessionAttr("customer", customer)
						.contentType(MediaType.APPLICATION_JSON).content(jsonObject))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
		Message message2 = objectMapper.readValue(result, Message.class);

		Map<String, String> map = (Map<String, String>) message2.getData();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			assertEquals(customer.getUserName(), entry.getValue());
			break;
		}
	}
}
