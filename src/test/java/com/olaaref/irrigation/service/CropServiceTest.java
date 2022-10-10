package com.olaaref.irrigation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
import com.olaaref.irrigation.exception.CropNotFoundException;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("/application-test.properties")
@SpringBootTest
public class CropServiceTest {

	@Autowired
	private JdbcTemplate jdbc;
	
	@Autowired
	private CropService cropService;
	
	@Value("${sql.script.create.crop}")
	private String addCropSql;
	
	@Value("${sql.script.delete.crop}")
	private String deleteCropSql;
	
	@BeforeEach
	public void setupDatabase() {
		jdbc.execute(addCropSql);
	}
	
	@AfterEach
	public void cleanDatabase() {
		jdbc.execute(deleteCropSql);
	}
	
	@Order(1)
	@DisplayName("Create Crop")
	@Test
	public void createCropTest() throws CropNotFoundException {
		Crop crop = new Crop("Tomato", 15, 5, 2);
		crop.setId(0);
		
		Crop savedCrop = cropService.saveCrop(crop);
		
		assertNotEquals(0, savedCrop.getId());

	}
	
	@Order(2)
	@DisplayName("Get Crop By ID")
	@Test
	public void getCropByIdTest() {	
		
		assertDoesNotThrow(() -> cropService.getCropById(1), "No Crop Found Exception");

	}
	
	@Order(3)
	@DisplayName("Get All Crops")
	@Test
	public void getAllCropsTest() {
		
		List<Crop> crops = cropService.getAllCrops();
		assertThat(crops).hasSizeGreaterThan(0);

	}
	
	@Order(4)
	@DisplayName("Delete Crop")
	@Test
	public void deleteCropTest() {

		assertDoesNotThrow(() -> cropService.deleteCrop(1), "No Crop Found Exception");
		
	}
}
