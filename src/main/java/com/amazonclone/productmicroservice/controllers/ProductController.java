package com.amazonclone.productmicroservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.amazonclone.productmicroservice.models.Area;
import com.amazonclone.productmicroservice.models.DeletedOrUpdateResponse;
import com.amazonclone.productmicroservice.models.Product;
import com.amazonclone.productmicroservice.services.ProductService;

@RestController
public class ProductController {
	
	@Autowired
	ProductService productService;


	@PostMapping("/product")
	public Product addNewProduct(@RequestBody Product product) {
		return productService.addProduct(product);
	}
	
	@PostMapping("/product/{productId}/area")
	public Product addNewShippableAddressToProduct(@PathVariable(value="productId") int productId, @RequestBody Area area) {
		return productService.addShippableArea(area,productId);
		//return null;
	}

	@GetMapping("/products")
	public List<Product> getAllProducts(){
		return productService.getAllProducts();
	}
	
	@GetMapping("/products/byId/{productId}")
	public Product getProductById(@PathVariable(value="productId") int productId) {
		return productService.getProduct(productId);
	}
	
	@GetMapping("/products/byName/{productName}")
	public List<Product> getProductByProductName(@PathVariable(value="productName") String productName) {
		return productService.getProductByName(productName);
	}
	
	@PutMapping("/product/{productId}/name")
	public Product updateProductName(@PathVariable(value="productId") int productId,@RequestBody Product product) {
		return productService.updateName(productId, product.getName());
	}
	
	@PutMapping("/product/{productId}/description")
	public Product updateProductDescription(@PathVariable(value="productId") int productId,@RequestBody Product product) {
		return productService.updateDescription(productId, product.getDescription());
	}
	
	@PutMapping("/product/{productId}/price")
	public Product updateProductPrice(@PathVariable(value="productId") int productId,@RequestBody Product product) {
		return productService.updatePrice(productId, product.getPrice());
	}
	
	@PutMapping("/product/{productId}/quantity")
	public Product updateProductQuantity(@PathVariable(value="productId") int productId,@RequestBody Product product) {
		return productService.updateQuantity(productId, product.getQuantity());
	}
	
	@PutMapping("/product/{productId}/category")
	public Product updateProductCategory(@PathVariable(value="productId") int productId,@RequestBody Product product) {
		return productService.updateCategory(productId, product.getCategory());
	}
	
	@DeleteMapping("/product/{productId}/{pincode}")
	public Product deleteProductShippableAreaByPincode(@PathVariable(value="productId") int productId,@PathVariable(value="pincode") int pincode) {
		return productService.deleteProductShippabaleAreaByPincode(productId,pincode);
	}
	
	@DeleteMapping("/product/{productId}")
	public ResponseEntity<DeletedOrUpdateResponse> deleteProductById(@PathVariable(value="productId") int productId) {
		DeletedOrUpdateResponse response = new DeletedOrUpdateResponse(false,"couldn't delete the product");
		
		if(productService.deleteProductById(productId)) {
			response.setSuccessful(true);
			response.setDescription("successfully deleted the product");
			return new ResponseEntity<DeletedOrUpdateResponse>(response,HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<DeletedOrUpdateResponse>(response,HttpStatus.BAD_REQUEST);
	}

}
