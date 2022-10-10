package com.olaaref.irrigation.batch;

import java.util.logging.Logger;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.olaaref.irrigation.config.IrrigationConfig;
import com.olaaref.irrigation.entity.Plot;

public class SaveLogProcessor implements ItemProcessor<Plot, Plot>{
	
	static final Logger LOGGER = Logger.getLogger(IrrigationConfig.class.getName());
	
	@Autowired
	private IrrigationConfig irrigationConfig;
	
	@Override
	public Plot process(Plot item) throws Exception {
		String msg = irrigationConfig.saveLogsOfPlot(item);
		LOGGER.info(msg);
		return item;
	}

}
