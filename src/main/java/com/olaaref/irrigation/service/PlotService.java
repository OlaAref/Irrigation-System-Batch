package com.olaaref.irrigation.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.olaaref.irrigation.entity.Crop;
import com.olaaref.irrigation.entity.Plot;
import com.olaaref.irrigation.exception.PlotNotFoundException;
import com.olaaref.irrigation.repository.PlotRepository;

@Service
@Transactional
public class PlotService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private PlotRepository plotRepository;

	public List<Plot> getAllPlots(){
		return plotRepository.findAll();
	}
	
	public Plot getById(Integer id) throws PlotNotFoundException {
		return plotRepository.findById(id).orElseThrow(() -> new PlotNotFoundException("No plot found with Id : " + id));
	}
	
	public Plot savePlot(Plot plot) {
		calculatePlotData(plot);
		
		return plotRepository.save(plot);
	}
	
	public void deletePlot(Integer id) throws PlotNotFoundException {
		if(plotRepository.findById(id).isEmpty()) throw new PlotNotFoundException("No plot found with id : "+id);
		plotRepository.deleteById(id);
	}
	
	private void calculatePlotData(Plot plot) {
		plot.setWaterAmount(plot.getCrop().getWaterAmount() * plot.getArea());
		plot.setDelay(plot.getCrop().getIrrigationDelay());
	}

	public Optional<List<Plot>> getByCrop(Crop crop) {
		return plotRepository.findByCrop(crop);
	}
	
	public void updatePlotCropData(Crop crop) {
		Optional<List<Plot>> plots = getByCrop(crop);
		if(plots.isPresent()) {
			plots.get().forEach((plot) -> {
				updatePlotCronAndWaterAmount(plot, crop);
			});
		}
		
	}

	private void updatePlotCronAndWaterAmount(Plot plot, Crop crop) {
		plot.setDelay(crop.getIrrigationDelay());
		double waterAmount = crop.getWaterAmount() * plot.getArea();
		plot.setWaterAmount(waterAmount);
	}

	public long countPlots() {
		return plotRepository.count();
	}
	
	public void updateSlotStatus(Integer id, boolean status) {
		plotRepository.updateSlotStatus(id, status);
	}
	
	public String sendRequest(Plot plot) {
		String url = "http://localhost:8085/IrrigationSystem/sensor/call/{id}";
		Map<String, String> params = new HashMap<>();
		params.put("id", plot.getId().toString());
		
		String message = restTemplate.getForObject(url, String.class, params);
		return message;
	}
	
	public String openSlot() {
		String url = "http://localhost:8085/IrrigationSystem/sensor/open";
		RestTemplate template = new RestTemplate();
		String message = template.getForObject(url, String.class);
		return message;
	}


	public String closeSlot() {
		String url = "http://localhost:8085/IrrigationSystem/sensor/close";
		RestTemplate template = new RestTemplate();
		String message = template.getForObject(url, String.class);
		return message;
	}
}

