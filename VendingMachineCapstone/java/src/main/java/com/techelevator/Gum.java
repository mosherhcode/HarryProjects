package com.techelevator;

import java.math.BigDecimal;

public class Gum extends Product {

	public Gum(String productLocation, String productName, BigDecimal productCost, String productType) {
		super(productLocation, productName, productCost, productType);
	}

	@Override
	public String getMessage() {
		String chew = "Chew Chew, Yum!";
		return chew;
	}

}