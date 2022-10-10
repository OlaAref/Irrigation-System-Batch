package com.olaaref.irrigation.sensorIntegration;

import java.util.logging.Logger;

import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import com.olaaref.irrigation.exception.SensorNotAvailableException;

@Service
public class SensorService {
	static final Logger LOGGER = Logger.getLogger(SensorService.class.getName());
	
	@Retryable
	public String sendRequest(Integer id) throws SensorNotAvailableException {
		
		if(Math.random() < .80) {
			LOGGER.warning("Retry to connect to sensor again ..");
			throw new SensorNotAvailableException("Sensor is not availabe, please try again later.");
		}
		return "irrigate request arrived to sensor from Plot "+id;
	}
	
	@Recover
	public String sendRequestFallback() {
		return "Calling Sensor failed.";
	}


	public String openSlot() {
		return "Slot is opened up.";
	}


	public String closeSlot() {
		return "Slot is opened closed.";
	}

}
