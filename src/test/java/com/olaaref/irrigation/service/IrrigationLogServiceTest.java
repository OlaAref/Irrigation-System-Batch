package com.olaaref.irrigation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

import com.olaaref.irrigation.entity.IrrigationLog;
import com.olaaref.irrigation.entity.Plot;
import com.olaaref.irrigation.exception.PlotNotFoundException;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("/application-test.properties")
@SpringBootTest
public class IrrigationLogServiceTest {

	@Autowired
	private JdbcTemplate jdbc;
	
	@Autowired
	private IrrigationLogService logService;
	
	@Autowired
	private PlotService plotService;
	
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
	public void createIrrigationLogTest() throws PlotNotFoundException {
		Plot plot = plotService.getById(1);
		IrrigationLog log = new IrrigationLog(plot, false);
		log.setId(0);
		
		IrrigationLog savedIrrigationLog = logService.saveLog(log);

		assertNotEquals(0, savedIrrigationLog.getId());
		assertFalse(log.isIrrigated());

	}
	
	@Order(2)
	@DisplayName("Get Irrigation Log By Plot")
	@Test
	public void getIrrigationLogByPlotTest() throws PlotNotFoundException {	
		List<IrrigationLog> logs = logService.findByPlot(1);
		assertEquals(1, logs.size());
	}
	
	
	@Order(3)
	@DisplayName("Get All Irrigation Logs")
	@Test
	public void getAllIrrigationLogsTest() {
		
		List<IrrigationLog> logs = logService.getAllLogs();
		assertThat(logs).hasSizeGreaterThan(0);

	}
	
}
