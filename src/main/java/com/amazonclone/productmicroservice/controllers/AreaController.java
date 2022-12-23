package com.amazonclone.productmicroservice.controllers;

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
import org.springframework.web.bind.annotation.RestController;

import com.amazonclone.productmicroservice.models.Area;
import com.amazonclone.productmicroservice.models.DeletedOrUpdateResponse;
import com.amazonclone.productmicroservice.models.dto.AreaDto;
import com.amazonclone.productmicroservice.repos.AreaRepo;
import com.amazonclone.productmicroservice.services.AreaService;
import com.amazonclone.productmicroservice.services.DTOMapper;


@RestController
public class AreaController {
	@Autowired
	AreaRepo areaRepo;
	
	@Autowired
	AreaService areaService;
	
	@Autowired
	DTOMapper dtoMapper;

	@PostMapping("/area")
	public AreaDto addArea(@RequestBody AreaDto areaDto) {
		Area area = dtoMapper.getAreaEntity(areaDto);
		areaRepo.save(area);
		return areaDto;
	}
	
	@GetMapping("/area")
	public List<Area> getAllAreas(){
		return areaRepo.findAll();
	}
	
	@GetMapping("/area/{city}")
	public List<Area> getAllAreasByCity(@PathVariable(value="city") String city){
		return areaRepo.findByCity(city);
	}
	
	@DeleteMapping("/area/{pincode}")
	public ResponseEntity<DeletedOrUpdateResponse> deleteAreaByPincode(@PathVariable(value="pincode") int pincode) {
		DeletedOrUpdateResponse response = new DeletedOrUpdateResponse(false,"No area exists with the specified pincode");
		if(areaService.deleteAreaByPincode(pincode)) {
			response.setSuccessful(true);
			response.setDescription(null);
			return new ResponseEntity<>(response,HttpStatus.OK);
		}
		return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
	}
	
	@DeleteMapping("/area/city/{city}")
	public ResponseEntity<DeletedOrUpdateResponse> deleteAreaByCity(@PathVariable(value="city") String city){
		DeletedOrUpdateResponse response = new DeletedOrUpdateResponse(false,"No areas to delete for the specified city");
		if(areaService.deleteAreaByCity(city)) {
			response.setSuccessful(true);
			response.setDescription(null);
			return new ResponseEntity<>(response,HttpStatus.OK);
		}
		return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
	}

	
	@PutMapping("/area/{oldCityName}/{newCityName}")
	public ResponseEntity<DeletedOrUpdateResponse> updateAllCityName(@PathVariable(value="oldCityName") String oldCityName, @PathVariable(value="newCityName") String newCityName) {
		DeletedOrUpdateResponse response = new DeletedOrUpdateResponse(false,"No areas to update for the specified city");
		if(areaService.updateAllCityName(oldCityName,newCityName)) {
			response.setSuccessful(true);
			response.setDescription(null);
			return new ResponseEntity<>(response,HttpStatus.OK);
		}
		return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
	}
	
}
