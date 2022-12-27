package com.amazonclone.productmicroservice.models.dto;


import java.util.Set;


import com.amazonclone.productmicroservice.models.Area;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class ProductDto {
	
	private int id;
	
	private String name;
	
	private String description;
	
	private String price;
	
	private long quantity;
	
	private String category;
	
	private Set<Area> shippableAreaPincodes;

}
