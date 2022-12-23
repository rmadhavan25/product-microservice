package com.amazonclone.productmicroservice.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AreaDto {
	private int pincode;
	private String city;
}
