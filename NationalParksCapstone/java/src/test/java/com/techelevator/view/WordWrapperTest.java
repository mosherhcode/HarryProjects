package com.techelevator.view;

import static org.junit.Assert.*;

import org.junit.Test;

public class WordWrapperTest {

	@Test
	public void testWrapMyStringFeedContinuosStringReturnHyphenation() {
		String expected = "1234567891234567891234567891234567891234567891234567891234567891234567891234567891234-\n567891234567891234567891234567891234567891234567891234567891234567891234567891234567-\n89123456789";
		String returned = WordWrapper.wrapMyString("123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789");
		
		assertEquals(expected, returned);
	}

	@Test
	public void testWrapMyStringFeedRegularParagraphReturnWrap() {
		String expected = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor\n" + 
				"incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis\n" + 
				"nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\n" + 
				"Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore\n" + 
				"eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt\n" + 
				"in culpa qui officia deserunt mollit anim id est laborum.";
		String returned = WordWrapper.wrapMyString("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
		
		assertEquals(expected, returned);
	}

	
}
