package com.olaaref.irrigation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.olaaref.irrigation.entity.Crop;
import com.olaaref.irrigation.exception.CropNotFoundException;
import com.olaaref.irrigation.repository.CropRepository;

@Service
@Transactional
public class CropService {

	@Autowired
	private CropRepository cropRepository;
	
	@Autowired
	private PlotService plotService;

	public List<Crop> getAllCrops() {
		return cropRepository.findAll();
	}
	
	public Crop getCropById(Integer id) throws CropNotFoundException {
		return cropRepository.findById(id).orElseThrow(() -> new CropNotFoundException("No Crop Found By Id : " + id));
	}
	
	public Crop saveCrop(Crop crop)  {
		Crop savedCrop = cropRepository.save(crop);
		
		if(crop.getId() != 0) {
			plotService.updatePlotCropData(savedCrop);
		}
		return savedCrop;
	}
	
	public void deleteCrop(Integer id) throws CropNotFoundException {
		if(cropRepository.findById(id).isEmpty()) throw new CropNotFoundException("No Crop Found By Id : " + id);
		cropRepository.deleteById(id);
	}

	
}
