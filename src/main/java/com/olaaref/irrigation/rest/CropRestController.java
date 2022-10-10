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


import com.olaaref.irrigation.entity.Crop;
import com.olaaref.irrigation.exception.CropNotFoundException;
import com.olaaref.irrigation.exception.IrrigationResponse;
import com.olaaref.irrigation.service.CropService;

@RestController
@RequestMapping("/crops")
public class CropRestController {

	@Autowired
	private CropService cropService;
	
	@GetMapping("/")
	public List<Crop> getAllCropTypes(){
		return cropService.getAllCrops();
	}
	
	@GetMapping("/{id}")
	public Crop getCropById(@PathVariable("id") Integer id) throws CropNotFoundException {
		return cropService.getCropById(id);
	}
	
	@PostMapping("/")
	public ResponseEntity<Crop> saveNewCrop(@RequestBody Crop crop) {
		crop.setId(0);
		Crop savedCrop = cropService.saveCrop(crop);
		return new ResponseEntity<Crop>(savedCrop, HttpStatus.CREATED);
	}
	
	@PutMapping("/")
	public Crop updateCrop(@RequestBody Crop crop) {
		return cropService.saveCrop(crop);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<IrrigationResponse> deleteCrop(@PathVariable("id") Integer id) throws CropNotFoundException{
		cropService.deleteCrop(id);
		IrrigationResponse response = new IrrigationResponse(HttpStatus.OK.value(), "Crop with id " + id + " deleted successfully.", System.currentTimeMillis());
		return new ResponseEntity<IrrigationResponse>(response, HttpStatus.OK);
	}
}
