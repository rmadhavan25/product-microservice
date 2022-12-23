package com.amazonclone.productmicroservice.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



import com.amazonclone.productmicroservice.models.Product;



public interface ProductRepo extends JpaRepository<Product, Integer> {
	public List<Product> findByCategory(String category);
	
	
	@Modifying
	@Query("update Product p set p.name = :name where p.id = :id")
	public int updateProductName(@Param(value="id") int id, @Param(value="name") String name);
	
	@Modifying
	@Query("update Product p set p.description = :description where p.id = :id")
	public int updateProductDescription(@Param(value="id") int id, @Param(value="description") String description);
	
	@Modifying
	@Query("update Product p set p.price = :price where p.id = :id")
	public int updateProductPrice(@Param(value="id") int id, @Param(value="price") String price);
	
	@Modifying
	@Query("update Product p set p.quantity = :quantity where p.id = :id")
	public int updateProductQuantity(@Param(value="id") int id, @Param(value="quantity") long quantity);
	
	@Modifying
	@Query("update Product p set p.category = :category where p.id = :id")
	public int updateProductCategory(@Param(value="id") int id, @Param(value="category") String category);


	public List<Product> findByNameContainingIgnoreCase(String productName);
	
}
