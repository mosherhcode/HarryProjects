package com.techelevator;

import java.math.BigDecimal;

public class Customer {

	private BigDecimal balance;
	
	public Customer() {
		balance = BigDecimal.valueOf(0.00);
	}

	public BigDecimal getBalance() {
		return balance;
	}
	
	public BigDecimal addToBalance(BigDecimal amount) {
		balance = balance.add(amount);
		return balance;
	}
	
	public BigDecimal subtractFromBalance(BigDecimal amount) {
		balance = balance.subtract(amount);
		return balance;
	}
	
	public void setBalance(BigDecimal newBalance) {
		this.balance = newBalance;
	}
	
	public String returnChange() {
		BigDecimal remainingBalance = BigDecimal.ZERO;
		int numQuarters = balance.divide(BigDecimal.valueOf(0.25)).intValue();
		
		remainingBalance = balance.remainder(BigDecimal.valueOf(.25));
		
		int numDimes = remainingBalance.divide(BigDecimal.valueOf(.10)).intValue();
		remainingBalance = remainingBalance.remainder(BigDecimal.valueOf(.10));
		
		int numNickels = remainingBalance.divide(BigDecimal.valueOf(.05)).intValue();
		remainingBalance = remainingBalance.remainder(BigDecimal.valueOf(.05));
		
		int numPennies = remainingBalance.divide(BigDecimal.valueOf(.01)).intValue();
		
		
		String message = "Here's your change: " + numQuarters + " quarters, " + numDimes 
				+ " dimes, " + numNickels + " nickels, " + numPennies + " pennies.";
		
		return message;
	}
}