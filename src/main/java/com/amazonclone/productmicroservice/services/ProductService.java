package com.amazonclone.productmicroservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonclone.productmicroservice.exceptions.ProductNotFoundException;
import com.amazonclone.productmicroservice.exceptions.ShippableAreaAlreadyExistsException;
import com.amazonclone.productmicroservice.models.Area;
import com.amazonclone.productmicroservice.models.Product;
import com.amazonclone.productmicroservice.repos.AreaRepo;
import com.amazonclone.productmicroservice.repos.ProductRepo;

@Service
@Transactional
public class ProductService {

	@Autowired
	ProductRepo productRepo;
	
	@Autowired
	AreaRepo areaRepo;

	private boolean isPresent = false;
	
	/*------------Post------------------*/
	
	//post product
	public Product addProduct(Product product) {
		return productRepo.save(product);
	}
	
	//post shippable areas to product
	public Product addShippableArea(Area area, int productId) {
		
		Product product;

		product = getProduct(productId);

		if(this.isShippableAreaAlreadyPresent(product, area)) {
			this.isPresent = false;
			throw new ShippableAreaAlreadyExistsException("The product is already being shipped to this location");
		}
		product.addShippableAreaPincodes(area);
			

		return productRepo.save(product);
	}
	
	
	/*------------Get------------------*/
	
	//get all products
	public List<Product> getAllProducts() {
		return productRepo.findAll();
	}
	
	//get product by id
	public Product getProduct(int id) {
		return productRepo.findById(id).orElseThrow(()->new ProductNotFoundException("No product Exists for the id: "+id)); //check lazy loading
	}
	
	//get products by category
	public List<Product> getListOfProductsByCategory(String category){
		return productRepo.findByCategory(category);
	}
	
	//get products by matching name
	public List<Product> getProductByName(String productName) {
		return productRepo.findByNameContainingIgnoreCase(productName);
	}
	
	/*------------Update/PUT------------------*/
	
	//update name
	public Product updateName(int id,String name) {
		productRepo.updateProductName(id, name);
		return getProduct(id);
	}
	
	//update description
	public Product updateDescription(int id,String description) {
		productRepo.updateProductDescription(id, description);
		return getProduct(id);
	}
	
	//update price
	public Product updatePrice(int id,String price) {
		productRepo.updateProductPrice(id, price);
		return getProduct(id);
	}

	//update quantity
	public Product updateQuantity(int id,long quantity) {
		productRepo.updateProductQuantity(id, quantity);
		return getProduct(id);
	}

	//update category
	public Product updateCategory(int id,String category) {
		productRepo.updateProductCategory(id, category);
		return getProduct(id);
	}

	/*------------Delete------------------*/
	
	//delete product(using id)
	public boolean deleteProductById(int productId) {
		if(!productRepo.findById(productId).isPresent())
			throw new ProductNotFoundException("No product Exists for the id: "+productId);
		productRepo.deleteById(productId);
		return true;
	}
	
	//delete shippableAreas from product
	public Product deleteProductShippabaleAreaByPincode(int productId, int pincode) {
		Product product = getProduct(productId);
		removeShippableArea(product, pincode);
		return productRepo.save(product);
	}
	
	/*-----------------UTILITY----------------*/
	public boolean isShippableAreaAlreadyPresent(Product product,Area area) {
		product.getShippableAreaPincodes().forEach(a -> {if(a.getPincode()==area.getPincode()) {
			this.isPresent= true;
		}});
		return this.isPresent;
	}
	
	public boolean removeShippableArea(Product product,int pincode) {
		return product.getShippableAreaPincodes().removeIf(x->x.getPincode()==pincode);
	}



	
	
}
