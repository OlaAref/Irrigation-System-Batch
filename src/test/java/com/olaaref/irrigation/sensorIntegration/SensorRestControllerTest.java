package com.olaaref.irrigation.sensorIntegration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
public class SensorRestControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	
	@Order(1)
	@DisplayName("Send Request To Sensor")
	@Test
	public void sendRequestToSensorTest() throws Exception {
		mockMvc
		.perform(get("/sensor/call/{id}", 1))
		.andExpect(status().isOk())//status 200
		.andDo(print());
	}
	
	@Order(2)
	@DisplayName("Open slot by the sensor")
	@Test
	public void openSlotTest() throws Exception {
		mockMvc
		.perform(get("/sensor/open"))
		.andExpect(status().isOk())//status 200
		.andDo(print());
	}
	
	@Order(3)
	@DisplayName("Close slot by the sensor")
	@Test
	public void closeSlotTest() throws Exception {
		mockMvc
		.perform(get("/sensor/close"))
		.andExpect(status().isOk())//status 200
		.andDo(print());
	}
	
	
	
	
	
}
