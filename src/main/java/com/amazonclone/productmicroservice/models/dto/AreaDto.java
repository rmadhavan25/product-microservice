package com.amazonclone.productmicroservice.models.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class AreaDto {
	private int pincode;
	private String city;
}
