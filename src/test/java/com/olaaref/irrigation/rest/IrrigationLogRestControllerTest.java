package com.olaaref.irrigation.rest;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

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
import com.olaaref.irrigation.entity.IrrigationLog;
import com.olaaref.irrigation.entity.Plot;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
public class IrrigationLogRestControllerTest {
	
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
	@Value("${sql.script.create.log}")
	private String addLogSql;
	
	@Value("${sql.script.delete.crop}")
	private String deleteCropSql;
	@Value("${sql.script.delete.plot}")
	private String deletePlotSql;
	@Value("${sql.script.delete.log}")
	private String deleteLogSql;
	
	@BeforeEach
	public void setupDatabase() {
		jdbc.execute(addCropSql);
		jdbc.execute(addPlotSql);
		jdbc.execute(addLogSql);
	}
	
	@AfterEach
	public void cleanDatabase() {
		jdbc.execute(deleteLogSql);
		jdbc.execute(deletePlotSql);
		jdbc.execute(deleteCropSql);
	}
	
	@Order(1)
	@DisplayName("Create Irrigation Log")
	@Test
	public void createLogTest() throws Exception {
		Plot plot = entityManager.find(Plot.class, 1);
		IrrigationLog log = new IrrigationLog(plot, false, LocalDateTime.now());
		log.setId(0);
		
		mockMvc
			.perform(post("/logs/")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(log)))
			.andExpect(status().isCreated())//status 201
			.andDo(print());
			

	}
	
	@Order(2)
	@DisplayName("Get Irrigation Log By Plot")
	@Test
	public void getLogsByPlotTest() throws Exception {	
		
		mockMvc
			.perform(get("/logs/plot/{id}", 1))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

	}
	
	
	@Order(3)
	@DisplayName("Get All Irrigation Logs")
	@Test
	public void getAllLogsTest() throws Exception {
		
		mockMvc
			.perform(get("/logs/"))
			.andExpect(status().isOk())//return status is 200
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))//body is json
			.andExpect(jsonPath("$", hasSize(1)))//the list has size 1
			.andDo(print());//print request & response

	}
	
	@Order(4)
	@DisplayName("Get Log For Plot Does Not Exist")
	@Test
	public void getLogDoesNotExistTest() throws Exception {

		mockMvc
			.perform(get("/logs/plot/{id}", 5))
			.andExpect(status().is4xxClientError())//404
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.status", is(404)))
			.andDo(print());
		
	}
	
	
}
