package com.olaaref.irrigation.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olaaref.irrigation.entity.Plot;
import com.olaaref.irrigation.exception.IrrigationResponse;
import com.olaaref.irrigation.exception.PlotNotFoundException;
import com.olaaref.irrigation.service.PlotService;

@RestController
@RequestMapping("/plots")
public class PlotRestController {
	
	@Autowired
	private PlotService plotService;

	@GetMapping("/")
	public List<Plot> getAllPlots(){
		return plotService.getAllPlots();
	}
	
	@GetMapping("/{id}")
	public Plot getById(@PathVariable("id") Integer id) throws PlotNotFoundException {
		return plotService.getById(id);
	}
	
	@PostMapping("/")
	public ResponseEntity<Plot> savePlot(@RequestBody Plot plot) {
		plot.setId(0);
		Plot savedPlot = plotService.savePlot(plot);
		
		return new ResponseEntity<Plot>(savedPlot, HttpStatus.CREATED);
	}
	
	@PutMapping("/")
	public Plot updatePlot(@RequestBody Plot plot) {
		
		return plotService.savePlot(plot);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<IrrigationResponse> deletePlot(@PathVariable("id") Integer id) throws PlotNotFoundException{
		plotService.deletePlot(id);
		IrrigationResponse response = new IrrigationResponse(HttpStatus.OK.value(), "Plot with ID " + id + " deleted successfully", System.currentTimeMillis());
		return new ResponseEntity<IrrigationResponse>(response, HttpStatus.OK);
	}
	
	
}



















