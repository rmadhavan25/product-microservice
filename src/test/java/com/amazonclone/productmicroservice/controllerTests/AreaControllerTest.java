package com.amazonclone.productmicroservice.controllerTests;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.amazonclone.productmicroservice.controllers.AreaController;
import com.amazonclone.productmicroservice.models.Area;
import com.amazonclone.productmicroservice.repos.AreaRepo;
import com.amazonclone.productmicroservice.services.AreaService;

@WebMvcTest(AreaController.class)
public class AreaControllerTest {

	@MockBean
	private AreaService areaService;
	
	@MockBean
	private AreaRepo areaRepo;
	
//	@Autowired
//	private ObjectMapper objectMapper;
	
	@Autowired
	private MockMvc mockMvc;
	
	//get areas by city
	@Test
	public void getAreasGivenACityName() throws Exception {
		//GIVEN
		String cityName = "Coimbatore";
		Area area1 = new Area(641038,"Coimbatore");
		Area area2 = new Area(641039,"Coimbatore");
		List<Area> areas = Arrays.asList(area1,area2);
		
		//WHEN
		when(areaRepo.findByCity(cityName)).thenReturn(areas);
		
		//EXPECTED
		mockMvc.perform(get("/area/{city}",cityName).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(areas.size()))
				.andDo(print());
	}
	
	//delete areas by pincode
	@Test
	public void deleteAreaGivenAPincode()throws Exception{
		//GIVEN
		int pincode = 641038;
		//Area area = new Area(641038,"Coimbatore");
		
		//WHEN
		when(areaService.deleteAreaByPincode(pincode)).thenReturn(true);
		
		
		//EXPECTED
		mockMvc.perform(delete("/area/{pincode}",pincode).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.successful").value(true))
				.andDo(print());
	}
	
	//delete areas by city
	@Test
	public void deleteAreaGivenACityName()throws Exception{
		//GIVEN
		String cityName = "Coimbatore";
		//Area area = new Area(641038,"Coimbatore");
		
		//WHEN
		when(areaService.deleteAreaByCity(cityName)).thenReturn(true);
		
		
		//EXPECTED
		mockMvc.perform(delete("/area/city/{city}",cityName).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.successful").value(true))
				.andDo(print());
	}
	
	//update area
	@Test
	public void updateAreasWithANewCityName()throws Exception{
		//GIVEN
		String newCityName = "Kovai";
		String oldCityName = "Coimbatore";
		
		//WHEN
		when(areaService.updateAllCityName(oldCityName, newCityName)).thenReturn(true);
		
		//EXPECTED
		mockMvc.perform(put("/area/{oldCityName}/{newCityName}",oldCityName,newCityName).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.successful").value(true))
				.andDo(print());
	}
	
	
}
	