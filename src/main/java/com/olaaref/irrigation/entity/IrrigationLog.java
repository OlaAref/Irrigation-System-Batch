package com.olaaref.irrigation.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "irrigation_logs")
public class IrrigationLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "plot_id")
	private Plot plot;
	
	@Column(name = "irrigated")
	private boolean irrigated;
	
	@Column(name = "irrigated_date")
	@CreationTimestamp
	private LocalDateTime irrigatedDate;

	public IrrigationLog() {
	}

	public IrrigationLog(Plot plot, boolean irrigated) {
		this.plot = plot;
		this.irrigated = irrigated;
	}

	

	public IrrigationLog(Plot plot, boolean irrigated, LocalDateTime irrigatedDate) {
		this.plot = plot;
		this.irrigated = irrigated;
		this.irrigatedDate = irrigatedDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Plot getPlot() {
		return plot;
	}

	public void setPlot(Plot plot) {
		this.plot = plot;
	}


	public boolean isIrrigated() {
		return irrigated;
	}

	public void setIrrigated(boolean irrigated) {
		this.irrigated = irrigated;
	}

	public LocalDateTime getIrrigatedDate() {
		return irrigatedDate;
	}

	public void setIrrigatedDate(LocalDateTime irrigatedDate) {
		this.irrigatedDate = irrigatedDate;
	}

	@Override
	public String toString() {
		return "IrrigationLog [irrigatedDate=" + irrigatedDate + ", plot=" + plot.getId() + ", irrigated=" + irrigated + "]";
	}
	
	
}
