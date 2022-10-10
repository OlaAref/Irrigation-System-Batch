package com.olaaref.irrigation.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.olaaref.irrigation.entity.Crop;

@Repository
public interface CropRepository extends JpaRepository<Crop, Integer> {

	
}
