package com.techelevator;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class DrinkTest {

	Product drink;
	
	@Test
	public void getMessage_verify_result() {
		drink = new Drink("A3", "Cola", BigDecimal.valueOf(1.25), "drink");
		
		String expected = "Glug Glug, Yum!";
		
		assertEquals(expected, drink.getMessage());
	}
}
