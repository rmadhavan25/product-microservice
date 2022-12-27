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
import com.amazonclone.productmicroservice.models.dto.AreaDto;
import com.amazonclone.productmicroservice.models.dto.ProductDto;
import com.amazonclone.productmicroservice.services.DTOMapper;
import com.amazonclone.productmicroservice.services.ProductService;

@RestController
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	DTOMapper dtoMapper;


	@PostMapping("/product")
	public Product addNewProduct(@RequestBody ProductDto productDto) {
		Product product = dtoMapper.getProductEntity(productDto);
		return productService.addProduct(product);
	}
	
	@PostMapping("/product/{productId}/area")
	public Product addNewShippableAddressToProduct(@PathVariable(value="productId") int productId, @RequestBody AreaDto areaDto) {
		Area area = dtoMapper.getAreaEntity(areaDto);
		return productService.addShippableArea(area,productId);
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
	public Product updateProductName(@PathVariable(value="productId") int productId,@RequestBody ProductDto productDto) {
		Product product = dtoMapper.getProductEntity(productDto);
		return productService.updateName(productId, product.getName());
	}
	
	@PutMapping("/product/{productId}/description")
	public Product updateProductDescription(@PathVariable(value="productId") int productId,@RequestBody ProductDto productDto) {
		Product product = dtoMapper.getProductEntity(productDto);
		return productService.updateDescription(productId, product.getDescription());
	}
	
	@PutMapping("/product/{productId}/price")
	public Product updateProductPrice(@PathVariable(value="productId") int productId,@RequestBody ProductDto productDto) {
		Product product = dtoMapper.getProductEntity(productDto);
		return productService.updatePrice(productId, product.getPrice());
	}
	
	@PutMapping("/product/{productId}/quantity")
	public Product updateProductQuantity(@PathVariable(value="productId") int productId,@RequestBody ProductDto productDto) {
		Product product = dtoMapper.getProductEntity(productDto);
		return productService.updateQuantity(productId, product.getQuantity());
	}
	
	@PutMapping("/product/{productId}/category")
	public Product updateProductCategory(@PathVariable(value="productId") int productId,@RequestBody ProductDto productDto) {
		Product product = dtoMapper.getProductEntity(productDto);
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
			return new ResponseEntity<>(response,HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}

}
