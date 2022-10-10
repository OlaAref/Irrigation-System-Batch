package com.olaaref.irrigation.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.olaaref.irrigation.config.IrrigationConfig;
import com.olaaref.irrigation.entity.Plot;

public class ConsumeDurationProcessor implements ItemProcessor<Plot, Plot> {
	
	@Autowired
	private IrrigationConfig irrigationConfig;

	@Override
	public Plot process(Plot item) throws Exception {
		irrigationConfig.sleepForIrrigationDuration(item);
		return item;
	}

}
