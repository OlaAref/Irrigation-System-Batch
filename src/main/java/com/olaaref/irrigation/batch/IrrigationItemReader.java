package com.olaaref.irrigation.batch;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import com.olaaref.irrigation.entity.Plot;


public class IrrigationItemReader extends JdbcCursorItemReader<Plot> implements ItemReader<Plot> {
	
	@Autowired
	private DataSource dataSource;
	
	public IrrigationItemReader() {
		setDataSource(dataSource);
		setSql("SELECT id, area, delay, water_amount FROM plots");
		setName("jdbcCursorItemReader");
		setRowMapper(new PlotRowMapper());
	}
	
	
}
