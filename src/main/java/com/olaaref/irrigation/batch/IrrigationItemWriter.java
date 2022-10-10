package com.olaaref.irrigation.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.olaaref.irrigation.entity.Plot;

public class IrrigationItemWriter implements ItemWriter<Plot> {

	@Override
	public void write(List<? extends Plot> items) throws Exception {
		System.out.println(items);
		
	}

}
