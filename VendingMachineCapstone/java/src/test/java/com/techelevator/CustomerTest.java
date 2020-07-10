package com.techelevator;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

public class CustomerTest {

	Customer theCustomer;

	@Before
	public void reinstantiate_customer() {
		theCustomer = new Customer();
	}

	@Test
	public void addToBalance_add_amounts() {
		assertEquals(BigDecimal.valueOf(1.00), theCustomer.addToBalance(BigDecimal.valueOf(1.00)));
		assertEquals(BigDecimal.valueOf(3.00), theCustomer.addToBalance(BigDecimal.valueOf(2.00)));
		assertEquals(BigDecimal.valueOf(18.00), theCustomer.addToBalance(BigDecimal.valueOf(15.00)));
		assertEquals(BigDecimal.valueOf(1018.00), theCustomer.addToBalance(BigDecimal.valueOf(1000.00)));
		assertEquals(BigDecimal.valueOf(1020.50), theCustomer.addToBalance(BigDecimal.valueOf(2.50)));
	}

	@Test
	public void subtractFromBalance_subtract_amounts() {
		theCustomer.addToBalance(BigDecimal.valueOf(50));

		assertEquals(BigDecimal.valueOf(49.00), theCustomer.subtractFromBalance(BigDecimal.valueOf(1.00)));
		assertEquals(BigDecimal.valueOf(46.50), theCustomer.subtractFromBalance(BigDecimal.valueOf(2.50)));
		assertEquals(BigDecimal.valueOf(30.51), theCustomer.subtractFromBalance(BigDecimal.valueOf(15.99)));
		assertEquals(BigDecimal.valueOf(30.01), theCustomer.subtractFromBalance(BigDecimal.valueOf(.50)));
	}

	@Test
	public void returnChange() {
		theCustomer.addToBalance(BigDecimal.valueOf(1.91));
		String expected = "Here's your change: 7 quarters, 1 dimes, 1 nickels, 1 pennies.";
		assertEquals(expected, theCustomer.returnChange());
	}

	@Test
	public void addNegativeBalance_add_negative_amounts_to_customer_balance() {
		assertEquals(BigDecimal.valueOf(-1.00), theCustomer.addToBalance(BigDecimal.valueOf(-1.00)));
		assertEquals(BigDecimal.valueOf(-4.00), theCustomer.addToBalance(BigDecimal.valueOf(-3.00)));
		assertEquals(BigDecimal.valueOf(-22.00), theCustomer.addToBalance(BigDecimal.valueOf(-18.00)));
		assertEquals(BigDecimal.valueOf(-1040.00), theCustomer.addToBalance(BigDecimal.valueOf(-1018.00)));
	}

	@Test
	public void subtractNegativeBalance_subtract_negative_amounts_from_customer_balance() {
		assertEquals(BigDecimal.valueOf(1.00), theCustomer.subtractFromBalance(BigDecimal.valueOf(-1.00)));
		assertEquals(BigDecimal.valueOf(4.00), theCustomer.subtractFromBalance(BigDecimal.valueOf(-3.00)));
		assertEquals(BigDecimal.valueOf(22.00), theCustomer.subtractFromBalance(BigDecimal.valueOf(-18.00)));
		assertEquals(BigDecimal.valueOf(1040.00), theCustomer.subtractFromBalance(BigDecimal.valueOf(-1018.00)));

	}
}
