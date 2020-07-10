package com.techelevator;

import java.math.BigDecimal;

public class Chip extends Product{

	
	//constructor
	public Chip(String productLocation, String productName, BigDecimal productCost, String productType) {
		super(productLocation, productName, productCost, productType);	
	}
	
	@Override
	public String getMessage() {
		String crunch = "Crunch Crunch, Yum!";
		return crunch;
	}
	
	
}
