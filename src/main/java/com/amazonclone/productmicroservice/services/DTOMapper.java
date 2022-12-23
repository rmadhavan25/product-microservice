package com.amazonclone.productmicroservice.services;

import org.springframework.stereotype.Service;

import com.amazonclone.productmicroservice.models.Area;
import com.amazonclone.productmicroservice.models.Product;
import com.amazonclone.productmicroservice.models.dto.AreaDto;
import com.amazonclone.productmicroservice.models.dto.ProductDto;

@Service
public class DTOMapper {
	
	//area entity
	public Area getAreaEntity(AreaDto areaDto) {
		return new Area(areaDto.getPincode(), areaDto.getCity());
	}
	
	//product entity
	public Product getProductDto(ProductDto productDto) {
		return new Product(productDto.getId(), productDto.getName(), productDto.getDescription(), productDto.getPrice(), productDto.getQuantity(), productDto.getCategory(), productDto.getShippableAreaPincodes());
	}
}
