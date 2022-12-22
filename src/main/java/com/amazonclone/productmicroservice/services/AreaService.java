package com.amazonclone.productmicroservice.services;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonclone.productmicroservice.models.Area;
import com.amazonclone.productmicroservice.repos.AreaRepo;

@Service
@Transactional
public class AreaService {

	@Autowired
	AreaRepo areaRepo;
	
	/*------------------------Delete-----------------------------------*/
	
	//delete area by pin-code
	public boolean deleteAreaByPincode(int pincode) {
		
		Optional<Area> areas = areaRepo.findById(pincode);
		Area area;
		
		if(areas.isPresent()) {
			area = areas.get();
			area.getProducts().forEach(product->product.getShippableAreaPincodes().removeIf(shippableArea->shippableArea.getPincode()==pincode));
			areaRepo.deleteByPincode(pincode);
			return true;
		}
		
		return false;
	}
	
	
	//delete all areas with given city name
	public boolean deleteAreaByCity(String city) {
		
		
		if(!areaRepo.findByCity(city).isEmpty()) {
			List<Area> areas = areaRepo.findByCity(city);
			areas.forEach(area->area.getProducts().forEach(product->product.getShippableAreaPincodes().removeIf(shipppableArea->shipppableArea.getCity().equals(city))));
			areaRepo.deleteByCity(city);
			return true;
		}
		

		return false;
	}

	/*------------------------Update/PUT-----------------------------------*/
	

	//update matching city names with new ones
	public boolean updateAllCityName(String oldCityName, String newCityName) {
		if(!areaRepo.findByCity(oldCityName).isEmpty()) {
			areaRepo.findByCity(oldCityName).forEach(area -> area.setCity(newCityName));
			return true;
		}
		return false;
	}
}
