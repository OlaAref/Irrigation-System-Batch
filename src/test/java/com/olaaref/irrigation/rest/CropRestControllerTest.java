package com.olaaref.irrigation.rest;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.olaaref.irrigation.entity.Crop;
import com.olaaref.irrigation.entity.Plot;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
public class CropRestControllerTest {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private JdbcTemplate jdbc;
	
	@Value("${sql.script.create.crop}")
	private String addCropSql;
	@Value("${sql.script.create.plot}")
	private String addPlotSql;
	
	@Value("${sql.script.delete.crop}")
	private String deleteCropSql;
	@Value("${sql.script.delete.plot}")
	private String deletePlotSql;
	
	@BeforeEach
	public void setupDatabase() {
		jdbc.execute(addCropSql);
		jdbc.execute(addPlotSql);
		
	}
	
	@AfterEach
	public void cleanDatabase() {
		jdbc.execute(deletePlotSql);
		jdbc.execute(deleteCropSql);
	}
	
	@Order(1)
	@DisplayName("Create Crop")
	@Test
	public void createCropTest() throws Exception {
		Crop crop = new Crop("Tomato", 15, 5, 2);
		crop.setId(0);
		
		mockMvc
			.perform(post("/crops/")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(crop)))
			.andExpect(status().isCreated())//status 201
			.andDo(print());
			

	}
	
	@Order(2)
	@DisplayName("Get Crop By ID")
	@Test
	public void getCropByIdTest() throws Exception {	
		
		mockMvc
			.perform(get("/crops/{id}", 1))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.type", is("Bean")))
			.andDo(print());

	}
	
	@Order(3)
	@DisplayName("Get All Crops")
	@Test
	public void getAllCropsTest() throws Exception {
		
		mockMvc
			.perform(get("/crops/"))
			.andExpect(status().isOk())//return status is 200
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))//body is json
			.andExpect(jsonPath("$", hasSize(1)))//the list has size 1
			.andDo(print());//print request & response

	}
	
	@Order(4)
	@DisplayName("Delete Crop")
	@Test
	public void deleteCropTest() throws Exception {

		mockMvc
			.perform(delete("/crops/{id}", 1))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andDo(print());
		
	}
	
	@Order(5)
	@DisplayName("Delete Crop Does Not Exist")
	@Test
	public void deleteCropDoesNotExistTest() throws Exception {

		mockMvc
			.perform(delete("/crops/{id}", 5))
			.andExpect(status().is4xxClientError())//404
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.status", is(404)))
			.andDo(print());
		
	}
	
	@Order(6)
	@DisplayName("Update Crop")
	@Test
	public void updateCropTest() throws Exception {
		Crop crop = new Crop("Tomato", 15, 5, 2);
		crop.setId(1);
		
		mockMvc
			.perform(put("/crops/")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(crop)))
			.andExpect(status().isOk())//status 200
			.andDo(print());
			
	}
	
	@Order(7)
	@DisplayName("Update Plot Data When Crop Get Updated")
	@Test
	public void updateCropAndPlotTest() throws Exception {
		
		Plot plotBefore = entityManager.find(Plot.class, 1);
		assertEquals(50, plotBefore.getWaterAmount());
		assertEquals(20, plotBefore.getDelay());
		
		//update crop
		Crop crop = entityManager.find(Crop.class, 1);
		crop.setIrrigationDelay(10);
		crop.setWaterAmount(3);
		
		mockMvc
			.perform(put("/crops/")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(crop)))
			.andExpect(status().isOk())
			.andDo(print());
			
		Plot plotAfter = entityManager.find(Plot.class, 1);
		assertEquals(60, plotAfter.getWaterAmount());
		assertEquals(10, plotAfter.getDelay());

	}
}
