package com.amazonclone.productmicroservice.serviceTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amazonclone.productmicroservice.exceptions.ProductNotFoundException;
import com.amazonclone.productmicroservice.exceptions.ShippableAreaAlreadyExistsException;
import com.amazonclone.productmicroservice.models.Area;
import com.amazonclone.productmicroservice.models.Product;
import com.amazonclone.productmicroservice.repos.ProductRepo;
import com.amazonclone.productmicroservice.services.ProductService;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
	
	@Mock
	private ProductRepo productRepo;
	
	@InjectMocks
	private ProductService productService;
	
	private Product product,product1;
	
	private Set<Area> shippableAreas;
	
	private Area area1,area2;
	
	@BeforeEach
	void setup() {
		area1 = new Area(641038,"Coimbatore");
		area2 = new Area(625020,"Madurai");
		shippableAreas = new HashSet<Area>();
		shippableAreas.add(area1);
		shippableAreas.add(area2);
		product =  new Product(1, "iphone", "apple mobile", "70000", 6, "smartphones", null);
		product1 = new Product(2,"predator 15","gaming laptop from acer","85000",9,"gaming",shippableAreas);
	}
	/*---------------------POST-------------------------*/
	
	//posting new product
	@Test
	void postProduct() {
		//given
		given(productRepo.save(product)).willReturn(product);
		
		//when
		Product savedProduct = productService.addProduct(product);
		
		//then
		assertThat(savedProduct).isEqualTo(product);
	}
	
	//posting new shippableArea to product
	@Test
	void addShippableAreaToProductTrue() {
		//given
		given(productRepo.save(product)).willReturn(product);
		given(productRepo.findById(product.getId())).willReturn(Optional.of(product));
		product.setShippableAreaPincodes(new HashSet<Area>());
		
		//when
		Product savedProduct = productService.addShippableArea(area1, product.getId());
		
		//then
		assertThat(savedProduct).isEqualTo(product);
		
	}
	
	//exception test
	@Test
	void shippableAreaAlreadyExistsException() {
		//given
		int productId = product.getId();
		given(productRepo.findById(product.getId())).willReturn(Optional.of(product));
		product.setShippableAreaPincodes(shippableAreas);
		
		//when
		assertThrows(ShippableAreaAlreadyExistsException.class,()->{
			productService.addShippableArea(area1, productId);
		});
		//then
		verify(productRepo,never()).save(any(Product.class));
	}
	
	/*---------------------GET-------------------------------*/
	
	
	//get list of all products
	@Test
	void getListOfProducts() {
		//given
		given(productRepo.findAll()).willReturn(Arrays.asList(product1,product));
		
		//when
		List<Product> retrievedProducts = productService.getAllProducts(); 
		
		//then
		assertThat(retrievedProducts).isEqualTo(Arrays.asList(product1,product));
	}
	
	//get product by id
	@Test
	void getProductByIdTrue(){
		//given
		given(productRepo.findById(product.getId())).willReturn(Optional.of(product));
		
		//when
		Product retrievedProduct = productService.getProduct(product.getId());
		
		//then
		assertThat(retrievedProduct).isEqualTo(product);
	}
	
	//exception: productNotFound
	@Test
	void productNotFoundException() {
		//given
		int productId = product.getId();
		given(productRepo.findById(product.getId())).willReturn(Optional.empty());
		
		//when
		assertThrows(ProductNotFoundException.class, ()->productService.getProduct(productId));
		
		//then
	}
	
	//get products by category
	@Test
	void getListOfProductsByCategory() {
		//given
		given(productRepo.findByCategory(product.getCategory())).willReturn(Arrays.asList(product));
		
		//when
		List<Product> retrievedProduts = productService.getListOfProductsByCategory(product.getCategory());
		
		//then
		assertThat(retrievedProduts).isEqualTo(Arrays.asList(product));
	}
	
	//get products by matching name
	@Test
	void getProductsByName() {
		//given
		given(productRepo.findByNameContainingIgnoreCase("predator")).willReturn(Arrays.asList(product1));
		
		//when
		List<Product> retrievedProduts = productService.getProductByName("predator");
		
		//then
		assertThat(retrievedProduts).isEqualTo(Arrays.asList(product1));
	}
	
	/*---------------------------PUT---------------------------*/
	
	//update name
	@Test
	void updateName() {
		//given
		given(productRepo.updateProductName(product.getId(), "iphone 12")).willReturn(1);
		product.setName("iphone 12");
		given(productRepo.findById(product.getId())).willReturn(Optional.of(product));
		
		//when
		Product result = productService.updateName(product.getId(), "iphone 12");
		
		//then
		assertThat(result.getName()).isEqualTo("iphone 12");
	}
	
	//update description
	@Test
	void updateDescription() {
		//given
		given(productRepo.updateProductDescription(product.getId(), "iphone from apple giant")).willReturn(1);
		product.setDescription("iphone from apple giant");
		given(productRepo.findById(product.getId())).willReturn(Optional.of(product));
		
		//when
		Product result = productService.updateDescription(product.getId(), "iphone from apple giant");
		
		//then
		assertThat(result.getDescription()).isEqualTo("iphone from apple giant");
	}
	
	//update price
	@Test
	void updatePrice() {
		//given
		given(productRepo.updateProductPrice(product.getId(), "90000")).willReturn(1);
		product.setPrice("90000");
		given(productRepo.findById(product.getId())).willReturn(Optional.of(product));
		
		//when
		Product result = productService.updatePrice(product.getId(), "90000");
		
		//then
		assertThat(result.getPrice()).isEqualTo("90000");
	}
	
	//update quantity
	@Test
	void updateQuantity() {
		//given
		given(productRepo.updateProductQuantity(product.getId(), 15)).willReturn(1);
		product.setQuantity(15);
		given(productRepo.findById(product.getId())).willReturn(Optional.of(product));
		
		//when
		Product result = productService.updateQuantity(product.getId(), 15);
		
		//then
		assertThat(result.getQuantity()).isEqualTo(15);
	}
	
	//update category
	@Test
	void updateCategory() {
		//given
		given(productRepo.updateProductCategory(product.getId(), "smartphones and iphones")).willReturn(1);
		product.setCategory("smartphones and iphones");
		given(productRepo.findById(product.getId())).willReturn(Optional.of(product));
		
		//when
		Product result = productService.updateCategory(product.getId(), "smartphones and iphones");
		
		//then
		assertThat(result.getCategory()).isEqualTo("smartphones and iphones");
	}
	
	/*---------------------------DELETE----------------------*/
	
	//delete product by id
	@Test
	void deleteProductByIdTrue() {
		//given
		given(productRepo.findById(product.getId())).willReturn(Optional.of(product));
		willDoNothing().given(productRepo).deleteById(product.getId());
		
		//when
		boolean result = productService.deleteProductById(product.getId());
		
		//then
		assertThat(result).isTrue();
	}
	
	//throws product not found exception
	@Test
	void throws_productNotFoundException_when_deleting() {
		//given
		int productId = product.getId();
		given(productRepo.findById(product.getId())).willReturn(Optional.empty());
		//when
		assertThrows(ProductNotFoundException.class, ()->{
			productService.deleteProductById(productId);
		});
		//then
		verify(productRepo,never()).deleteById(product.getId());
	}
	
	//deleting shippable area from product
	@Test
	void deleteShippableAreaFromProduct() {
		//given
		given(productRepo.findById(product1.getId())).willReturn(Optional.of(product1));
		given(productRepo.save(product1)).willReturn(product1);
		//when
		Product resultProduct = productService.deleteProductShippabaleAreaByPincode(product1.getId(), 641038);
		
		//then
		assertThat(resultProduct).isEqualTo(product1);
	}

	/*---------------------------UTILITY----------------------*/
	
	@Test
	void testIsShippableAreaPresentMethodTrue() {
		//given
		product.setShippableAreaPincodes(new HashSet<Area>());
		product.addShippableAreaPincodes(area1);
		//when
		boolean result = productService.isShippableAreaAlreadyPresent(product, area1);
		//then
		assertThat(result).isTrue();
	}
	
	@Test
	void testIsShippableAreaPresentMethodFalse() {
		//given
		product.setShippableAreaPincodes(new HashSet<Area>());
		//when
		boolean result = productService.isShippableAreaAlreadyPresent(product, area1);
		//then
		assertThat(result).isFalse();
	}
	
	@Test
	void removeShippableAreaFromProductTrue() {
		//given
		product1.setShippableAreaPincodes(shippableAreas);
		
		//when
		boolean result = productService.removeShippableArea(product1, 641038);
		
		//then
		assertThat(result).isTrue();
	}
	
	@Test
	void removeShippableAreaFromProductFalse() {
		//given
		shippableAreas.remove(area1);
		product1.setShippableAreaPincodes(shippableAreas);
		
		//when
		boolean result = productService.removeShippableArea(product1, 641038);
		
		//then
		assertThat(result).isFalse();
	}
}
