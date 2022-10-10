package com.olaaref.irrigation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.olaaref.irrigation.entity.Crop;
import com.olaaref.irrigation.entity.Plot;


@Repository
public interface PlotRepository extends JpaRepository<Plot, Integer> {

	public Optional<List<Plot>> findByCrop(Crop crop);

	@Modifying
	@Query("UPDATE Plot p SET p.slot = ?2 WHERE p.id = ?1")
	public void updateSlotStatus(Integer id, boolean status);

}
