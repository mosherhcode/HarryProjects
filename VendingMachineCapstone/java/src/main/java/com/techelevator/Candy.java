package com.techelevator;

import java.math.BigDecimal;

public class Candy extends Product {

	public Candy(String productLocation, String productName, BigDecimal productCost, String productType) {
		super(productLocation, productName, productCost, productType);
	}

	@Override
	public String getMessage() {
		String munch = "Munch Munch, Yum!";
		return munch;
	}

}
