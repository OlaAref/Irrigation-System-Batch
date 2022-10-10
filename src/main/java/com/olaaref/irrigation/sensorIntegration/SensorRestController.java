package com.olaaref.irrigation.sensorIntegration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olaaref.irrigation.exception.SensorNotAvailableException;

@RestController
@RequestMapping("/sensor")
public class SensorRestController {
	
	@Autowired
	private SensorService sensorService;

	@GetMapping("/call/{id}")
	public String callSensor(@PathVariable("id") Integer id) throws SensorNotAvailableException{
		return sensorService.sendRequest(id);
	}
	
	@GetMapping("/open")
	public String openSprinklers(){
		return sensorService.openSlot();
	}
	
	@GetMapping("/close")
	public String closeSprinklers(){
		return sensorService.closeSlot();
	}
	
}



















