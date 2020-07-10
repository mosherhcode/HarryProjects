package com.techelevator;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class GumTest {

	Product gum;
	
	@Test
	public void getMessage_verify_result() {
		gum = new Gum("A4", "U-Chews", BigDecimal.valueOf(0.85), "gum");
		
		String expected = "Chew Chew, Yum!";
		
		assertEquals(expected, gum.getMessage());
	}
}
