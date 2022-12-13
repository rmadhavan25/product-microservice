package com.amazonclone.productmicroservice.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amazonclone.productmicroservice.models.Seller;


@Repository
public interface SellerRepo extends JpaRepository<Seller, Integer> {

}
