package com.amazonclone.productmicroservice.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amazonclone.productmicroservice.models.Area;


@Repository
public interface AreaRepo extends JpaRepository<Area, Integer> {
	public List<Area> findByCity(String city);
	
	public int deleteByPincode(int pincode);
	
	public int deleteByCity(String city);
}
