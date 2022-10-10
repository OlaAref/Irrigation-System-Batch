package com.olaaref.irrigation.config;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.olaaref.irrigation.entity.Crop;
import com.olaaref.irrigation.entity.IrrigationLog;
import com.olaaref.irrigation.entity.Plot;
import com.olaaref.irrigation.exception.CropNotFoundException;
import com.olaaref.irrigation.service.CropService;
import com.olaaref.irrigation.service.IrrigationLogService;
import com.olaaref.irrigation.service.PlotService;


@Component
public class IrrigationConfig {
	static final Logger LOGGER = Logger.getLogger(IrrigationConfig.class.getName());
	
	@Autowired
	private PlotService plotService;
	
	@Autowired
	private CropService cropService;
	
	@Autowired
	private IrrigationLogService logService;
	
	public String sendRequestToSensor(Plot plot) {
		return plotService.sendRequest(plot);
	}

	public String chengeSlotToClose(Plot plot) {
		String closeMsg = plotService.closeSlot();
		plotService.updateSlotStatus(plot.getId(), false);
		LOGGER.info(closeMsg);
		return closeMsg;
	}

	public String changeSlotToOpen(Plot plot) {
		String openMsg = plotService.openSlot();
		plotService.updateSlotStatus(plot.getId(), true);
		LOGGER.info(openMsg);
		return openMsg;
	}
	
	public void sleepForIrrigationDuration(Plot plot) throws InterruptedException, CropNotFoundException {
		Crop crop = cropService.getCropById(plot.getCropId());
		long durationInMilli = crop.getIrrigationDuration() * 1000;
		Thread.sleep(durationInMilli);
	}

	
	public String saveLogsOfPlot(Plot plot) {
		IrrigationLog log = new IrrigationLog(plot, true);
		IrrigationLog savedLog = logService.saveLog(log);
		String msg = String.format("The Plot %d has been irrigated successfully at %s.", savedLog.getPlot().getId(), savedLog.getIrrigatedDate().toString());
		return msg;
	}
	
	
	
}
