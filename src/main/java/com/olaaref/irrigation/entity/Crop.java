package com.olaaref.irrigation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "crops")
public class Crop {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "vegetation_type", length = 128, nullable = false, unique = true)
	private String type;
	
	@Column(name = "irrigation_delay", nullable = false)
	private int irrigationDelay;//the delay between every two irrigations by seconds(for testing purpose)
	
	@Column(name = "irrigation_duration", nullable = false)
	private int irrigationDuration;// irrigation duration by seconds
	
	@Column(name = "water_amount", nullable = false)
	private double waterAmount;//amount of water lit/m3

	public Crop() {
	}

	public Crop(Integer id) {
		this.id = id;
	}
	
	public Crop(String type, int irrigationDelay, int irrigationDuration, double waterAmount) {
		this.type = type;
		this.irrigationDelay = irrigationDelay;
		this.irrigationDuration = irrigationDuration;
		this.waterAmount = waterAmount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getIrrigationDelay() {
		return irrigationDelay;
	}

	public void setIrrigationDelay(int irrigationDelay) {
		this.irrigationDelay = irrigationDelay;
	}

	public int getIrrigationDuration() {
		return irrigationDuration;
	}

	public void setIrrigationDuration(int irrigationDuration) {
		this.irrigationDuration = irrigationDuration;
	}

	public double getWaterAmount() {
		return waterAmount;
	}

	public void setWaterAmount(double waterAmount) {
		this.waterAmount = waterAmount;
	}

	@Override
	public String toString() {
		return "Crop [id=" + id + ", type=" + type + ", irrigationDelay=" + irrigationDelay + ", irrigationDuration="
				+ irrigationDuration + ", waterAmount=" + waterAmount + "]";
	}
	
	
}
