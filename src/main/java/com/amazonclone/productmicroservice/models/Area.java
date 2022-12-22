package com.amazonclone.productmicroservice.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Area {
	
	@Id
	private int pincode;
	
	private String city;
	
	@ManyToMany(fetch = FetchType.LAZY,
		      cascade = {
		              CascadeType.PERSIST,
		              CascadeType.MERGE
		          },mappedBy = "shippableAreaPincodes")
	@JsonIgnore
	private Set<Product> products = new HashSet<>();
	
	public Area(int pincode,String city){
		this.pincode = pincode;
		this.city = city;
	}
	
}
