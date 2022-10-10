package com.olaaref.irrigation.batch;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import com.olaaref.irrigation.entity.Plot;

public class PlotRowMapper implements RowMapper<Plot> {
	
	
	@Override
	public Plot mapRow(ResultSet rs, int rowNum) throws SQLException {
		Plot plot  = new Plot();
		plot.setId(rs.getInt("id"));
		plot.setArea(rs.getDouble("area"));
		plot.setWaterAmount(rs.getDouble("water_amount"));
		plot.setDelay(rs.getInt("delay"));
		plot.setCropId(rs.getInt("crop_id"));
		
		return plot;
	}
	
}