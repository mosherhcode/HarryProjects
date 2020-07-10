package com.techelevator;

import java.math.BigDecimal;

public class Drink extends Product {

	public Drink(String productLocation, String productName, BigDecimal productCost, String productType) {
		super(productLocation, productName, productCost, productType);
	}

	@Override
	public String getMessage() {
		String glug = "Glug Glug, Yum!";
		return glug;
	}

}
