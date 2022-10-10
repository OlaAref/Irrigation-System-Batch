package com.olaaref.irrigation.rest;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olaaref.irrigation.entity.Crop;
import com.olaaref.irrigation.entity.Plot;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
public class PlotRestControllerTest {
	
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
	@DisplayName("Create Plot")
	@Test
	public void createPlotTest() throws Exception {
		Crop crop = entityManager.find(Crop.class, 1);
		Plot plot = new Plot(crop, 15);
		plot.setId(0);
		
		mockMvc
			.perform(post("/plots/")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(plot)))
			.andExpect(status().isCreated())//201
			.andExpect(jsonPath("$.id", is(2)))
			.andDo(print());
			

	}
	
	@Order(2)
	@DisplayName("Get Plot By ID")
	@Test
	public void getPlotByIdTest() throws Exception {	
		
		mockMvc
			.perform(get("/plots/{id}", 1))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id", is(1)))
			.andDo(print());

	}
	
	@Order(3)
	@DisplayName("Get All Plots")
	@Test
	public void getAllPlotsTest() throws Exception {
		
		mockMvc
			.perform(get("/plots/"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$", hasSize(1)))
			.andDo(print());

	}
	
	@Order(4)
	@DisplayName("Delete Plot")
	@Test
	public void deletePlotTest() throws Exception {

		mockMvc
			.perform(delete("/plots/{id}", 1))
			.andExpect(status().isOk())
			.andDo(print());
		
	}
	
	@Order(5)
	@DisplayName("Get Plot Does Not Exist")
	@Test
	public void getPlotDoesNotExistTest() throws Exception {

		mockMvc
			.perform(get("/plots/{id}", 111))
			.andExpect(status().is4xxClientError())//404
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andDo(print());
		
	}
	
	@Order(6)
	@DisplayName("Update Plot")
	@Test
	public void updatePlotTest() throws Exception {
		Plot plot = entityManager.find(Plot.class, 1);
		assertEquals(20, plot.getArea());
		
		//update plot
		plot.setArea(25);
		
		mockMvc
			.perform(put("/plots/")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(plot)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.area", is(25.0)))
			.andDo(print());
	}
	
	
}
