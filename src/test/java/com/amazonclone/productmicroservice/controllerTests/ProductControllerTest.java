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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
class ProductControllerTest {
	
	@MockBean
	private ProductService productService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	/*---------------------------POST---------------------------------------*/
	
	//add new product
	@Test
	void shouldCreateAProduct() throws JsonProcessingException, Exception {
		Product product = new Product(1, "iphone", "apple mobile", "70000", 6, "smartphones", null);
		
		mockMvc.perform(post("/product").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(product)))
				.andExpect(status().isOk())
				.andDo(print());
	}
	
	//post shippable areas to products
	@Test
	void addNewShippableAddressToProduct() throws Exception {
		//GIVEN
		Area area = new Area(641038,"Coimbatore");
		int productId = 1;
		Set<Area> shippableAreaPincodes = new HashSet<Area>();
		shippableAreaPincodes.add(area);
		Product product = new Product(1, "iphone", "apple mobile", "70000", 6, "smartphones", shippableAreaPincodes);
		
		//WHEN
		when(productService.addShippableArea(any(Area.class), eq(productId))).thenReturn(product);
		
		//EXPECTED
		mockMvc.perform(post("/product/{productId}/area",productId).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(area)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(product.getId()))
				.andExpect(jsonPath("$.name").value(product.getName()))
				.andExpect(jsonPath("$.price").value(product.getPrice()))
				.andExpect(jsonPath("$.description").value(product.getDescription()))
				.andExpect(jsonPath("$.category").value(product.getCategory()))
				.andExpect(jsonPath("$.quantity").value(product.getQuantity()))
				.andExpect(jsonPath("$.shippableAreaPincodes.size()").value(product.getShippableAreaPincodes().size()))
				.andDo(print());
	}
	
	
	/*---------------------------GET---------------------------------------*/
	
	//getting product by id
	@Test
	void getProductById() throws Exception {
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
	
	//get list of all products
	@Test
	void getListOfAllProducts() throws Exception {
		Product product = new Product(1, "iphone", "apple mobile", "70000", 6, "smartphones", null);
		Product product1 = new Product(2, "iphone 12", "old apple mobile", "40000", 7, "smartphones", null);
		
		List<Product> products =  Arrays.asList(product,product1);
		
		when(productService.getAllProducts()).thenReturn(products);
		
		mockMvc.perform(get("/products").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.size()").value(products.size()))
		.andDo(print());
	}
	
	//getting list of products given a search string
	@Test
	void getProductsMatchingSearchString() throws Exception {
		Product product = new Product(1, "iphone", "apple mobile", "70000", 6, "smartphones", null);
		Product product1 = new Product(2, "iphone 12", "old apple mobile", "40000", 7, "smartphones", null);
		
		List<Product> products =  Arrays.asList(product,product1);
		
		when(productService.getProductByName("iphone")).thenReturn(products);
		
		mockMvc.perform(get("/products/byName/{productName}","iphone").contentType(MediaType.APPLICATION_JSON)
				).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(products.size()))
				.andDo(print());
	}
	
	
	/*---------------------------PUT---------------------------------------*/
	
	//updating product name
	@Test
	void shouldUpdateProductName() throws Exception {
		Product product = new Product(1, "iphone", "apple mobile", "70000", 6, "smartphones", null);
		Product updatedProduct = new Product(1, "iphone 14", "apple mobile", "70000", 6, "smartphones", null);
		
		when(productService.updateName(product.getId(),updatedProduct.getName())).thenReturn(updatedProduct);
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
	
	//updating product description
	@Test
	void shouldUpdateProductDescription() throws Exception {
		Product product = new Product(1, "iphone", "apple mobile", "70000", 6, "smartphones", null);
		Product updatedProduct = new Product(1, "iphone", "apple made mobile", "70000", 6, "smartphones", null);
		
		when(productService.updateDescription(product.getId(),updatedProduct.getDescription())).thenReturn(updatedProduct);
		int productId = product.getId();
		
		mockMvc.perform(put("/product/{productId}/description",productId).contentType(MediaType.APPLICATION_JSON)
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
	
	//updating product price
	@Test
	void shouldUpdateProductPrice() throws Exception {
		Product product = new Product(1, "iphone", "apple mobile", "70000", 6, "smartphones", null);
		Product updatedProduct = new Product(1, "iphone", "apple mobile", "75000", 6, "smartphones", null);
		
		when(productService.updatePrice(product.getId(),updatedProduct.getPrice())).thenReturn(updatedProduct);
		int productId = product.getId();
		
		mockMvc.perform(put("/product/{productId}/price",productId).contentType(MediaType.APPLICATION_JSON)
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
	
	//updating product quantity
	@Test
	void shouldUpdateProductQuantity() throws Exception {
		Product product = new Product(1, "iphone", "apple mobile", "70000", 6, "smartphones", null);
		Product updatedProduct = new Product(1, "iphone", "apple mobile", "70000", 8, "smartphones", null);
		
		when(productService.updateQuantity(product.getId(),updatedProduct.getQuantity())).thenReturn(updatedProduct);
		int productId = product.getId();
		
		mockMvc.perform(put("/product/{productId}/quantity",productId).contentType(MediaType.APPLICATION_JSON)
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
	
	//updating product category
	@Test
	void shouldUpdateProductCategory() throws Exception {
		Product product = new Product(1, "iphone", "apple mobile", "70000", 6, "smartphones", null);
		Product updatedProduct = new Product(1, "iphone", "apple mobile", "70000", 6, "iPhones & smartphones", null);
		
		when(productService.updateCategory(product.getId(),updatedProduct.getCategory())).thenReturn(updatedProduct);
		int productId = product.getId();
		
		mockMvc.perform(put("/product/{productId}/category",productId).contentType(MediaType.APPLICATION_JSON)
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
	
	/*---------------------------DELETE---------------------------------------*/
	
	//delete shippable area from product
	@Test
	void deleteShippableAreaFromProduct()throws Exception{
		//GIVEN
		int productId = 1;
		int pincode = 641038;
		Set<Area> shippableAreaPincodes = new HashSet<Area>();
		Product product = new Product(1, "iphone", "apple mobile", "70000", 6, "smartphones", shippableAreaPincodes);
		
		//WHEN
		when(productService.deleteProductShippabaleAreaByPincode(productId, pincode)).thenReturn(product);
		
		//EXPECTED
		mockMvc.perform(delete("/product/{productId}/{pincode}",productId,pincode).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(product.getId()))
		.andExpect(jsonPath("$.name").value(product.getName()))
		.andExpect(jsonPath("$.price").value(product.getPrice()))
		.andExpect(jsonPath("$.description").value(product.getDescription()))
		.andExpect(jsonPath("$.category").value(product.getCategory()))
		.andExpect(jsonPath("$.quantity").value(product.getQuantity()))
		.andExpect(jsonPath("$.shippableAreaPincodes.size()").value(product.getShippableAreaPincodes().size()))
		.andDo(print());
		
	}
	
	//deleting product
	@Test
	void shouldDeleteProductByIdTrue() throws Exception {
		int productId = 1;
		when(productService.deleteProductById(productId)).thenReturn(true);
		mockMvc.perform(delete("/product/{productId}",productId))
		.andExpect(status().isNoContent())
		.andExpect(jsonPath("$.successful").value(true))
		.andExpect(jsonPath("$.description").value("successfully deleted the product"))
		.andDo(print());
	}
	
	@Test
	void shouldDeleteProductByIdFalse() throws Exception {
		int incorrectProductId = 1;
		when(productService.deleteProductById(incorrectProductId)).thenReturn(false);
		mockMvc.perform(delete("/product/{productId}",incorrectProductId))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.successful").value(false))
		.andExpect(jsonPath("$.description").value("couldn't delete the product"))
		.andDo(print());
	}
	
	
	/*---------------------------EXCEPTIONS---------------------------------------*/
	
	//product not found exception for given id
	@Test
	void throwProductNotFoundException() throws Exception {
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
	void throwShippableAreaAlreadyExistsException() throws Exception {
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
