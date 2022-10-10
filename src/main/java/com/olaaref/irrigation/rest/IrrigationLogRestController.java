package com.olaaref.irrigation.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olaaref.irrigation.entity.IrrigationLog;
import com.olaaref.irrigation.exception.PlotNotFoundException;
import com.olaaref.irrigation.service.IrrigationLogService;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/logs")
public class IrrigationLogRestController {

	@Autowired
	private IrrigationLogService logService;
	
	@GetMapping("/")
	public List<IrrigationLog> getAllLogs(){
		return logService.getAllLogs();
	}
	
	@GetMapping("/plot/{id}")
	public List<IrrigationLog> getByPlot(@PathVariable("id") Integer id) throws PlotNotFoundException{
		return logService.findByPlot(id);
	}

	
	@PostMapping("/")
	public ResponseEntity<IrrigationLog> createLog(@RequestBody IrrigationLog log){
		log.setId(0);
		IrrigationLog savedLog = logService.saveLog(log);
		
		return new ResponseEntity<IrrigationLog>(savedLog, HttpStatus.CREATED);
	}
}
