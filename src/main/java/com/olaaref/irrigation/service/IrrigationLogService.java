package com.olaaref.irrigation.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.olaaref.irrigation.entity.IrrigationLog;
import com.olaaref.irrigation.entity.Plot;
import com.olaaref.irrigation.exception.PlotNotFoundException;
import com.olaaref.irrigation.repository.IrrigationLogRepository;

@Service
@Transactional
public class IrrigationLogService {

	@Autowired
	private IrrigationLogRepository logRepository;
	
	@Autowired
	private PlotService plotService;
 
	
	public List<IrrigationLog> getAllLogs(){
		return logRepository.findAll();
	}
	
	public List<IrrigationLog> findByPlot(Integer plotId) throws PlotNotFoundException{
		Plot plot = plotService.getById(plotId);
		return logRepository.findByPlot(plot).orElse(Collections.emptyList());
	}
	

	
	public IrrigationLog saveLog(IrrigationLog log) {
		return logRepository.save(log);
	}
}
