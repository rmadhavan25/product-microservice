package com.amazonclone.productmicroservice.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String name;
	
	private String description;
	
	private String price;
	
	private long quantity;
	
	private String category;
	
	@ManyToMany(fetch = FetchType.LAZY,
		      cascade = {
		    	  CascadeType.PERSIST,
		          CascadeType.MERGE,
		      })
	@JoinTable(name="product_available_area",
				joinColumns = @JoinColumn(name = "product_id"),
				inverseJoinColumns = @JoinColumn(name = "pincode"))
	private Set<Area> shippableAreaPincodes = new HashSet<Area>();
	
	public void addShippableAreaPincodes(Area area) {
		System.out.println(this.name);
		this.shippableAreaPincodes.add(area);
		area.getProducts().add(this);
	}
	
//	@ManyToMany(mappedBy = "products")
//	private List<Seller> sellers;

}
