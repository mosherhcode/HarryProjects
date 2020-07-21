package com.techelevator.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import org.junit.Before;
import org.junit.Test;

public class MenuTest {
	
	private ByteArrayOutputStream output;
	
	@Before
	public void setup() {
		output = new ByteArrayOutputStream();
	}
	

	
	@Test
	public void test_verify_menu_returns_options_with_header_and_footer() {
		Object[] options = new Object[] {"A", "B", "C"};
		Menu menu = getMenuForTestingWithUserInput("1\r\n");
		
		menu.getChoiceFromOptions(options, "Test Header", "Test Footer", "%3s", false);
		
		String expected = "\r\nTest Header\n  1 "+ options[0].toString() +"\n  2 "+options[1].toString()+"\n  3 "+options[2].toString()+"\n\nTest Footer ";
		
		assertEquals(expected, output.toString());
	}
	
	@Test
	public void test_verify_menu_returns_options_with_header_and_footer_and_quit() {
		Object[] options = new Object[] {"A", "B", "C"};
		Menu menu = getMenuForTestingWithUserInput("1\r\n");
		
		menu.getChoiceFromOptions(options, "Test Header", "Test Footer", "%3s", true);
		
		String expected = "\r\nTest Header\n  1 "+ options[0].toString() +"\n  2 "+options[1].toString()+"\n  3 "+options[2].toString()+"\n  Q Quit\n\nTest Footer ";
		
		assertEquals(expected, output.toString());
	}

	@Test 
	public void test_get_true_from_user() {
		Menu menu = getMenuForTestingWithUserInput("y\n");
		boolean actual = menu.getYTrueOrNFalseFromUser("Test Request Text");
		assertTrue(actual);
	}
	
	@Test 
	public void test_get_false_from_user() {
		Menu menu = getMenuForTestingWithUserInput("n\n");
		boolean actual = menu.getYTrueOrNFalseFromUser("Test Request Text");
		assertFalse(actual);
	}
	
	@Test 
	public void test_get_boolean_from_user_invalid_entry() {
		Menu menu = getMenuForTestingWithUserInput("t\r\ny\n");
		menu.getYTrueOrNFalseFromUser("Test Request Text");
		String expected = "Test Request Text(Y or N) Invalid entry.\n\r\nTest Request Text(Y or N) ";
		
		assertEquals(expected,output.toString());
	}
	
	@Test
	public void test_get_long_from_user_successfully() {
		Menu menu = getMenuForTestingWithUserInput("15\n");
		long actual = menu.getLongFromUser("Test Request Text");
		assertEquals(15, actual);
	}
	
	@Test
	public void test_get_long_from_user_invalid_entry() {
		Menu menu = getMenuForTestingWithUserInput("X\n15");
		menu.getLongFromUser("Test Request Text");
		String expected = "Test Request Text Invalid number format. Please enter again.\n\r\nTest Request Text ";
		assertEquals(expected, output.toString());
	}
	
	@Test
	public void test_get_long_from_user_negative_number() {
		Menu menu = getMenuForTestingWithUserInput("-10\n15");
		menu.getLongFromUser("Test Request Text");
		String expected = "Test Request Text Please enter a positive number.\n\r\nTest Request Text ";
		assertEquals(expected, output.toString());
	}

	
	///IN PROGRESS
	
	@Test
	public void test_get_date_from_user_invalid_entry_then_proper_entries() {
		Menu menu = getMenuForTestingWithUserInput("10\n5/20/2022\n5/25/2022\n");
		menu.getDateRangeFromUser("What is your party's arrival date?", "What is your party's departure date?");
		String expectedOutput = "What is your party's arrival date? (in format MM/DD/YYYY) There was an issue with the date you entered.\n\r\n"
				+ "What is your party's arrival date? (in format MM/DD/YYYY) You entered Friday, May 20, 2022\n\r\n"
				+ "What is your party's departure date? (in format MM/DD/YYYY) ";
		assertEquals(expectedOutput, output.toString());
	}
	
	@Test
	public void test_get_date_from_user_shortcut_proper_entries() {
		LocalDate fromDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue() + 1, 5);
		LocalDate toDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue() + 1, 12);
		Menu menu = getMenuForTestingWithUserInput("6\n7\n");
		menu.getDateRangeFromUser("What is your party's arrival date?", "What is your party's departure date?");
		String expectedOutput = "What is your party's arrival date? (in format MM/DD/YYYY) You entered "  + fromDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)) + "\n\r\n"
				+ "What is your party's departure date? (in format MM/DD/YYYY) ";
		assertEquals(expectedOutput, output.toString());
	}
	
	@Test
	public void test_get_string_from_user() {
		Menu menu = getMenuForTestingWithUserInput("Hello\n");
		String actual = menu.getStringFromUser("Test Request Text");
		String expectedOutput = "Test Request Text ";
		assertEquals("Hello", actual);
		assertEquals(expectedOutput, output.toString());
	}
	
	@Test
	public void test_print_string_to_console(){
		Menu menu = getMenuForTestingWithUserInput("");
		menu.printStringToConsoleLn("Test String");
		String expectedOutput = "Test String\r\n";
		assertEquals(expectedOutput, output.toString());
	}
	
	@Test
	public void test_print_section_break_to_console() {
		Menu menu = getMenuForTestingWithUserInput("");
		menu.printSectionBreakToConsole();
		String expectedOutput = "\n\n--------------------------------------------------------------------------------\n\n";
		assertEquals(expectedOutput, output.toString());
	}
	
	private Menu getMenuForTestingWithUserInput(String userInput) {
		ByteArrayInputStream input = new ByteArrayInputStream(String.valueOf(userInput).getBytes());
		return new Menu(input, output);
	}
	
//	Prints out a string as a series of hex characters. 
//	Good for seeing where the actual differences are between 2 strings.
//	private static void printBytes( String st){
//		String bytes="";
//		for(byte it: st.getBytes())
//		bytes+=Integer.toHexString(it)+" ";
//		System.out.println("HexValues: "+bytes);
//		}
}
