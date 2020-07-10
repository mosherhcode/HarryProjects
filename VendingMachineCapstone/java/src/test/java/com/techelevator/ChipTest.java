package com.techelevator;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class ChipTest {

	Product chip;
	
	@Test
	public void getMessage_verify_result() {
		chip = new Chip("A2", "Potato Crisps", BigDecimal.valueOf(3.05), "chip");
		
		String expected = "Crunch Crunch, Yum!";
		
		assertEquals(expected, chip.getMessage());
	}
}
