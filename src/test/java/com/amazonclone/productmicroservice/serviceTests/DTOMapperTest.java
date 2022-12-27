package com.amazonclone.productmicroservice.serviceTests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amazonclone.productmicroservice.models.Area;
import com.amazonclone.productmicroservice.models.Product;
import com.amazonclone.productmicroservice.models.dto.AreaDto;
import com.amazonclone.productmicroservice.models.dto.ProductDto;
import com.amazonclone.productmicroservice.services.DTOMapper;

@ExtendWith(MockitoExtension.class)
class DTOMapperTest {
	
	@InjectMocks
	private DTOMapper dtoMapper;
	
	@Test
	void getAreaEntity() {
		//given
		AreaDto areaDto = new AreaDto(625020,"madurai");
		Area area = new Area(625020,"madurai");
		
		//when
		Area mappedArea = dtoMapper.getAreaEntity(areaDto);
		
		//then
		assertThat(mappedArea.getPincode()).isEqualTo(area.getPincode());
		assertThat(mappedArea.getCity()).isEqualTo(area.getCity());
		
	}
	
	@Test
	void getProductEntity() {
		//given
		ProductDto productDto = new ProductDto(1, "iphone", "apple mobile", "70000", 6, "smartphones", new HashSet<>());
		Product product = new Product(1, "iphone", "apple mobile", "70000", 6, "smartphones", new HashSet<>());
		
		//when
		Product mappedProduct = dtoMapper.getProductEntity(productDto);
		
		//then
		assertThat(mappedProduct.getId()).isEqualTo(product.getId());
		assertThat(mappedProduct.getName()).isEqualTo(product.getName());
		assertThat(mappedProduct.getShippableAreaPincodes()).isEqualTo(product.getShippableAreaPincodes());
	}
}
