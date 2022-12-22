package com.amazonclone.productmicroservice.serviceTests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.amazonclone.productmicroservice.models.Area;
import com.amazonclone.productmicroservice.models.Product;
import com.amazonclone.productmicroservice.repos.AreaRepo;
import com.amazonclone.productmicroservice.services.AreaService;


@ExtendWith(MockitoExtension.class)
class AreaServiceTest {

	@Mock
	private AreaRepo areaRepo;
	
	@InjectMocks
	private AreaService areaService;
	
	private Area area1,area2,area3,area4;
	
	private List<Area> areas,areasWithSameCityName;
	
	@BeforeEach
	void setup() {
		area1 = new Area(641038, "coimbatore");
		area2 = new Area(625020,"madurai");
		area3 = new Area(600028, "chennai");
		area4 = new Area(641039, "coimbatore");
		
		areas = Arrays.asList(area1,area2,area3);
		areasWithSameCityName = Arrays.asList(area1,area4);
	}
	
	/*--------------------------UPDATE--------------------------*/
	
	//updateAllCityName
	@Test
	void update_all_city_name_true() {
		//given
		String oldCityName = "coimbatore";
		String newCityName = "kovai";
		given(areaRepo.findByCity(oldCityName)).willReturn(areasWithSameCityName);
		
		//when
		boolean result = areaService.updateAllCityName(oldCityName, newCityName);
		
		//then
		assertThat(result).isTrue();
	}
	
	@Test
	void update_all_city_name_false() {
		//given
		String oldCityName = "coimbatore";
		String newCityName = "kovai";
		given(areaRepo.findByCity(oldCityName)).willReturn(new ArrayList<Area>());
		
		//when
		boolean result = areaService.updateAllCityName(oldCityName, newCityName);
		
		//then
		assertThat(result).isFalse();
	}
	
	/*--------------------------DELETE--------------------------*/
	
	//delete all areas with given city name
	@Test
	void deleteAreaByCityNameTrue() {
		//given
		String cityName = "coimbatore";
		given(areaRepo.findByCity(cityName)).willReturn(areasWithSameCityName);
		given(areaRepo.deleteByCity(cityName)).willReturn(2);
		
		//when
		boolean result = areaService.deleteAreaByCity(cityName);
		
		//then
		assertThat(result).isTrue();
	}
	
	@Test
	void deleteAreaByCityNameFalse() {
		//given
		String cityName = "coimbatore";
		given(areaRepo.findByCity(cityName)).willReturn(new ArrayList<Area>());
		
		//when
		boolean result = areaService.deleteAreaByCity(cityName);
		
		//then
		assertThat(result).isFalse();
	}
	
	//delete area by id
	@Test
	void deleteAreaByIdTrue() {
		//given
		given(areaRepo.findById(641038)).willReturn(Optional.of(area1));
		given(areaRepo.deleteByPincode(641038)).willReturn(1);
		Set<Area> shippableAreas = new HashSet<Area>();
		shippableAreas.add(area1);
		shippableAreas.add(area2);
		Product product1 =  new Product(1, "iphone", "apple mobile", "70000", 6, "smartphones", shippableAreas);
		Product product2 = new Product(2,"predator 15","gaming laptop from acer","85000",9,"gaming",shippableAreas);
		Set<Product> products = new HashSet<Product>();
		products.add(product1);
		products.add(product2);
		area1.setProducts(products);
		
		//when
		boolean result = areaService.deleteAreaByPincode(641038);
		
		//then
		assertThat(product1.getShippableAreaPincodes()).doesNotContain(area1);
		assertThat(product2.getShippableAreaPincodes()).doesNotContain(area1);
		assertThat(result).isTrue();
	}
	
	@Test
	void deleteAreaByIdFalse() {
		//given
		given(areaRepo.findById(641038)).willReturn(Optional.empty());
		
		//when
		boolean result = areaService.deleteAreaByPincode(641038);
		
		//then
		assertThat(result).isFalse();
	}
	
}
