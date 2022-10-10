package com.olaaref.irrigation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import com.olaaref.irrigation.entity.Crop;
import com.olaaref.irrigation.entity.Plot;
import com.olaaref.irrigation.exception.CropNotFoundException;
import com.olaaref.irrigation.exception.PlotNotFoundException;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("/application-test.properties")
@SpringBootTest
public class PlotServiceTest {

	@Autowired
	private JdbcTemplate jdbc;
	
	@Autowired
	private PlotService plotService;
	
	@Autowired
	private CropService cropService;
	
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
	public void createPlotTest() throws PlotNotFoundException, CropNotFoundException {
		Crop crop = cropService.getCropById(1);
		Plot plot = new Plot(crop, 15);
		plot.setId(0);
		
		Plot savedPlot = plotService.savePlot(plot);

		assertNotEquals(0, savedPlot.getId());
		assertEquals("Bean", savedPlot.getCrop().getType());

	}
	
	@Order(2)
	@DisplayName("Get Plot By ID")
	@Test
	public void getPlotByIdTest() {	
		
		assertDoesNotThrow(() -> plotService.getById(1), "No Plot Found Exception");

	}
	
	@Order(3)
	@DisplayName("Get All Plots")
	@Test
	public void getAllPlotsTest() {
		
		List<Plot> plots = plotService.getAllPlots();
		assertThat(plots).hasSizeGreaterThan(0);

	}
	
	@Order(4)
	@DisplayName("Delete Plot")
	@Test
	public void deletePlotTest() {

		assertDoesNotThrow(() -> plotService.deletePlot(1), "No Plot Found Exception");
		
	}
	
	@Order(4)
	@DisplayName("Update Plot Data When Crop Get Updated")
	@Test
	public void updaetPlotTest() throws CropNotFoundException, PlotNotFoundException {
		//plot before update
		Plot plotBefore = plotService.getById(1);
		assertEquals(20, plotBefore.getDelay());
		
		//update crop
		Crop crop = cropService.getCropById(1);
		crop.setIrrigationDelay(10);
		Crop savedCrop = cropService.saveCrop(crop);
		assertEquals(10, savedCrop.getIrrigationDelay());
		
		//plot after update
		Plot plotAfter = plotService.getById(1);
		assertEquals(10, plotAfter.getDelay());
		
	}
	
	@Order(5)
	@DisplayName("Count Plots")
	@Test
	public void getPlotsCountTest(){
		
		assertEquals(1, plotService.countPlots());
		
	}
}
