package com.amazonclone.productmicroservice;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductMicroserviceApplicationTests {

	@Test
	void contextLoads() {
		assertTrue(true);
	}
	
	@Test
	void applicationContetTest() {
		ProductMicroserviceApplication.main(new String[] {});
		assertTrue(true);
	}

}
