package com.amazonclone.productmicroservice.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Seller {
	
	@Id
	@GeneratedValue
	private int id;
	
	private String name;
	
	private String phone;
	 
	private String email;
	
	private String address;
	
	private String about;
	
	@ManyToMany
	@JoinTable(name = "product_ownership",
				joinColumns = @JoinColumn(name="seller_id"),
				inverseJoinColumns = @JoinColumn(name="product_id"))
	private List<Product> products;
	
	
}
