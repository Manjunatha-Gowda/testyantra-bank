
package com.te.testyantrabank.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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
import com.te.testyantrabank.dto.Admin;
import com.te.testyantrabank.message.Message;
import com.te.testyantrabank.service.AdminService;

@SpringBootTest
class AdminControllerTest {
	protected MockMvc mvc;

	@InjectMocks
	private AdminController adminController;

	@Mock
	private AdminService adminService;
	private MockMvc mockMvc;
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
		this.objectMapper = new ObjectMapper();
	}

	@Test
	void testGetAllCustomer() throws UnsupportedEncodingException, Exception {
		Admin admin = new Admin("Cherry", "admin8861");
		Message message = new Message();
		message.setData(admin);
		Mockito.when(adminService.getAllCustomer()).thenReturn(message);
		String jsonObject = objectMapper.writeValueAsString(admin);
		String result = mockMvc
				.perform(get("/api/v1/admin/customers").contentType(MediaType.APPLICATION_JSON).content(jsonObject))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
		Message message2 = objectMapper.readValue(result, Message.class);
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) message2.getData();
		for (Map.Entry<String, String> m : map.entrySet()) {
			assertEquals(admin.getUserName(), m.getValue());
			break;
		}
	}
}
