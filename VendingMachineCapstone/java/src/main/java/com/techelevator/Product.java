package com.techelevator;

import java.math.BigDecimal;

public class Product {
	private String productLocation, productName, productType;
	private BigDecimal productCost;
	private int stockAmount;
	
	public Product(String productLocation, String productName, BigDecimal productCost, 
			String productType) {
		this.productLocation = productLocation;
		this.productName = productName;
		this.productCost = productCost;
		this.productType = productType;
		this.stockAmount = 5;
	}

	public String getProductLocation() {
		return productLocation;
	}

	public void setProductLocation(String productLocation) {
		this.productLocation = productLocation;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public BigDecimal getProductCost() {
		return productCost;
	}

	public void setProductCost(BigDecimal productCost) {
		this.productCost = productCost;
	}

	public int getStockAmount() {
		return stockAmount;
	}

	public void setStockAmount(int stockAmount) {
		this.stockAmount = stockAmount;
	}
	
	@Override
	public String toString() {
		String stringToReturn = String.format("(%s) %-20s $%-7.2f", this.getProductLocation(), this.getProductName(), this.getProductCost().setScale(2));
		
		//check to see if the item is out of stock
		if(this.getStockAmount() > 0)
			stringToReturn += " (" + this.getStockAmount() + " in stock)";
		else
			stringToReturn += " SOLD OUT";
		
		return stringToReturn;
	}
	
	public String getMessage() {
		return "Chomp Chomp, Yum!";
	}
	
	
}
