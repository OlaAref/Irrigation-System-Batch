package com.olaaref.irrigation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class IrrigationSystemRestExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<IrrigationResponse> handleException(CropNotFoundException e){
		IrrigationResponse response = new IrrigationResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<IrrigationResponse> handleEcxeption(PlotNotFoundException e){
		IrrigationResponse response = new IrrigationResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler
	public ResponseEntity<IrrigationResponse> handleEcxeption(Exception e){
		IrrigationResponse response = new IrrigationResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
}
