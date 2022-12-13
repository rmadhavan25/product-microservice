package com.amazonclone.productmicroservice.controllerTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.amazonclone.productmicroservice.controllers.ProductController;
import com.amazonclone.productmicroservice.exceptions.ProductNotFoundException;
import com.amazonclone.productmicroservice.exceptions.ShippableAreaAlreadyExistsException;
import com.amazonclone.productmicroservice.models.Area;
import com.amazonclone.productmicroservice.models.Product;
import com.amazonclone.productmicroservice.services.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
	
	@MockBean
	private ProductService productService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	void shouldCreateAProduct() throws JsonProcessingException, Exception {
		Product product = new Product(1, "iphone", "apple mobile", "70000", 6, "smartphones", null);
		
		mockMvc.perform(post("/product").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(product)))
				.andExpect(status().isOk())
				.andDo(print());
	}
	
	//updating of product
	@Test
	void shouldUpdateProductName() throws Exception {
		Product product = new Product(1, "iphone", "apple mobile", "70000", 6, "smartphones", null);
		Product updatedProduct = new Product(1, "iphone 14", "apple mobile", "70000", 6, "smartphones", null);
		
		when(productService.updateName(product.getId(),"iphone 14")).thenReturn(updatedProduct);
		int productId = product.getId();
		
		mockMvc.perform(put("/product/{productId}/name",productId).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedProduct))).andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(updatedProduct.getId()))
		.andExpect(jsonPath("$.name").value(updatedProduct.getName()))
		.andExpect(jsonPath("$.price").value(updatedProduct.getPrice()))
		.andExpect(jsonPath("$.description").value(updatedProduct.getDescription()))
		.andExpect(jsonPath("$.category").value(updatedProduct.getCategory()))
		.andExpect(jsonPath("$.quantity").value(updatedProduct.getQuantity()))
		.andExpect(jsonPath("$.shippableAreaPincodes").value(updatedProduct.getShippableAreaPincodes()))
		.andDo(print());
	}
	
	//deleting product
	@Test
	public void shouldDeleteProductById() throws Exception {
		int productId = 1;
		when(productService.deleteProductById(productId)).thenReturn(true);
		mockMvc.perform(delete("/product/{productId}",productId))
		.andExpect(status().isNoContent())
		.andExpect(jsonPath("$.successful").value(true))
		.andExpect(jsonPath("$.description").value("successfully deleted the product"))
		.andDo(print());
	}
	
	
	//getting product by id and all
	@Test
	public void getProductById() throws Exception {
		int productId = 1;
		Product product = new Product(1, "iphone", "apple mobile", "70000", 6, "smartphones", null);
		
		when(productService.getProduct(productId)).thenReturn(product);
		
		mockMvc.perform(get("/products/byId/{productId}",productId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(product.getId()))
				.andExpect(jsonPath("$.name").value(product.getName()))
				.andExpect(jsonPath("$.price").value(product.getPrice()))
				.andExpect(jsonPath("$.description").value(product.getDescription()))
				.andExpect(jsonPath("$.category").value(product.getCategory()))
				.andExpect(jsonPath("$.quantity").value(product.getQuantity()))
				.andExpect(jsonPath("$.shippableAreaPincodes").value(product.getShippableAreaPincodes()))
				.andDo(print());
	}
	
	@Test
	public void getListOfAllProducts() throws Exception {
		Product product = new Product(1, "iphone", "apple mobile", "70000", 6, "smartphones", null);
		Product product1 = new Product(2, "iphone 12", "old apple mobile", "40000", 7, "smartphones", null);
		
		List<Product> products =  Arrays.asList(product,product1);
		
		when(productService.getAllProducts()).thenReturn(products);
		
		mockMvc.perform(get("/products").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.size()").value(products.size()))
		.andDo(print());
	}
	
	//product not found exception for given id
	
	@Test
	public void throwProductNotFoundException() throws Exception {
		int productId = 1;
		
		
		when(productService.getProduct(productId)).thenThrow(new ProductNotFoundException("No product Exists for the id: "+productId));
		
		mockMvc.perform(get("/products/byId/{productId}",productId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.statusCode").value(404))
				.andExpect(jsonPath("$.message").value("No product Exists for the id: "+productId))
				.andDo(print());
	}
	
	//shippable area already exists exception
	@Test
	public void throwShippableAreaAlreadyExistsException() throws Exception {
		int productId = 1;
		
		Area area = new Area();
		area.setPincode(625020);
		area.setCity("madurai");
		
		when(productService.addShippableArea(any(Area.class),eq(productId))).thenThrow(new ShippableAreaAlreadyExistsException("The product is already being shipped to this location"));
		
		mockMvc.perform(post("/product/{productId}/area",productId).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(area)))
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.statusCode").value(403))
				.andExpect(jsonPath("$.message").value("The product is already being shipped to this location"))
				.andDo(print());
	}
}
