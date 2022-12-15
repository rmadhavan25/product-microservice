package com.amazonclone.productmicroservice.controllerTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AreaController.class)
public class AreaControllerTest {

	@MockBean
	private AreaService areaService;
	
	@MockBean
	private AreaRepo areaRepo;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private MockMvc mockMvc;
	
	//post area
	@Test
	public void postNewArea() throws Exception {
		//GIVEN
		Area area = new Area(625020,"Madurai");
		
		//WHEN
		when(areaRepo.save(any(Area.class))).thenReturn(area);
		
		//EXPECTED
		mockMvc.perform(post("/area").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(area)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.pincode").value(area.getPincode()))
				.andExpect(jsonPath("$.city").value(area.getCity()))
				.andDo(print());
	}
	
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
	
	//getting all areas
	@Test
	public void getAllAreas() throws Exception {
		//GIVEN
		Area area1 = new Area(641038,"Coimbatore");
		Area area2 = new Area(641039,"Coimbatore");
		Area area3 = new Area(625020,"Madurai");
		List<Area> areas = Arrays.asList(area1,area2,area3);
		
		//WHEN
		when(areaRepo.findAll()).thenReturn(areas);
		
		//EXPECTED
		mockMvc.perform(get("/area").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(areas.size()))
				.andDo(print());
	}
	
	//delete areas by pincode
	@Test
	public void deleteAreaGivenAPincodeTrue()throws Exception{
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
	@Test
	public void deleteAreaGivenAPincodeFalse()throws Exception{
		//GIVEN
		int incorrectPincode = 641038;
		//Area area = new Area(641038,"Coimbatore");
		
		//WHEN
		when(areaService.deleteAreaByPincode(incorrectPincode)).thenReturn(false);
		
		
		//EXPECTED
		mockMvc.perform(delete("/area/{pincode}",incorrectPincode).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.successful").value(false))
				.andExpect(jsonPath("$.description").value("No area exists with the specified pincode"))
				.andDo(print());
	}
	
	//delete areas by city
	@Test
	public void deleteAreaGivenACityNameTrue()throws Exception{
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
	@Test
	public void deleteAreaGivenACityNameFale()throws Exception{
		//GIVEN
		String incorrectCityName = "Coimbatore";
		//Area area = new Area(641038,"Coimbatore");
		
		//WHEN
		when(areaService.deleteAreaByCity(incorrectCityName)).thenReturn(false);
		
		
		//EXPECTED
		mockMvc.perform(delete("/area/city/{city}",incorrectCityName).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.successful").value(false))
				.andExpect(jsonPath("$.description").value("No areas to delete for the specified city"))
				.andDo(print());
	}
	
	//update area
	@Test
	public void updateAreasWithANewCityNameTrue()throws Exception{
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
	
	@Test
	public void updateAreasWithANewCityNameFalse()throws Exception{
		//GIVEN
		String newCityName = "Kovai";
		String inCorrectOldCityName = "Coimbatore";
		
		//WHEN
		when(areaService.updateAllCityName(inCorrectOldCityName, newCityName)).thenReturn(false);
		
		//EXPECTED
		mockMvc.perform(put("/area/{oldCityName}/{newCityName}",inCorrectOldCityName,newCityName).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.successful").value(false))
				.andExpect(jsonPath("$.description").value("No areas to update for the specified city"))
				.andDo(print());
	}
	
	
}
	