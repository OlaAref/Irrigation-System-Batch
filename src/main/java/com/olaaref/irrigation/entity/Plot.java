package com.olaaref.irrigation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "plots")
public class Plot {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "crop_id")
	private Crop crop;
	
	@Column(name = "area", nullable = false)
	private double area;
	
	@Column(name = "water_amount", nullable = false)
	private double waterAmount;
	
	@Column(name="delay", nullable = false)
	private int delay;
	
	@Column(name = "slot")
	private boolean slot;
	
	@Transient
	private Integer cropId;
	
	
	public Plot() {
	}

	public Plot(Crop crop, double area) {
		this.crop = crop;
		this.area = area;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Crop getCrop() {
		return crop;
	}

	public void setCrop(Crop crop) {
		this.crop = crop;
	}

	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
	}

	public double getWaterAmount() {
		return waterAmount;
	}

	public void setWaterAmount(double waterAmount) {
		this.waterAmount = waterAmount;
	}


	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}


	public boolean isSlot() {
		return slot;
	}

	public void setSlot(boolean slot) {
		this.slot = slot;
	}

	@Override
	public String toString() {
		return "Plot [id=" + id +  ", area=" + area + ", waterAmount=" + waterAmount + ", delay=" + delay  + "]";
	}

	public Integer getCropId() {
		return cropId;
	}

	public void setCropId(Integer cropId) {
		this.cropId = cropId;
	}
	

}
