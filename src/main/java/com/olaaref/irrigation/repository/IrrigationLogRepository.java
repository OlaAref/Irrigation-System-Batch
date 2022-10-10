package com.olaaref.irrigation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.olaaref.irrigation.entity.IrrigationLog;
import com.olaaref.irrigation.entity.Plot;

@Repository
public interface IrrigationLogRepository extends JpaRepository<IrrigationLog, Integer> {

	public Optional<List<IrrigationLog>> findByPlot(Plot plot);


}
