package com.techelevator;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class CandyTest {
	Product candy;
	
	@Test
	public void getMessage_verify_result() {
		candy = new Candy("A1", "Wonka Bar", BigDecimal.valueOf(1.50), "candy");
		
		String expected = "Munch Munch, Yum!";
		
		assertEquals(expected, candy.getMessage());
	}

}
