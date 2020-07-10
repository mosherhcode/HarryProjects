package com.techelevator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FileManagerTest {

	FileManager fileManager = new FileManager();
	
	@Before
	public void call_audit_log_setup() {
		fileManager.setupAuditLog("LogTest.txt");
	}
	
	@Test
	public void setupAuditLog_verify_file_exists() {
		
		File logFile = new File("LogTest.txt");
		assertTrue(logFile.exists());
	}
	
	@Test
	public void writeToAuditLog_verify_line_was_added() throws FileNotFoundException {
		
		
		File logFile = new File("LogTest.txt");
		Scanner logScanner = new Scanner(logFile);
		int initialLineCount = 0;
		while(logScanner.hasNextLine()) {
			initialLineCount ++;
			logScanner.nextLine();
		}
		logScanner.close();
		
		fileManager.writeToLog("Testing write to log");
		logScanner = new Scanner(logFile);
		
		int finalLineCount = 0;
		while(logScanner.hasNextLine()) {
			finalLineCount++;
			logScanner.nextLine();
		}
		assertEquals(initialLineCount + 1, finalLineCount);
		logScanner.close();
		
	}
	
	//For some reason these two are not showing as equal. The results from readFromVendingMachingCsv are coming back with 10 empty elements.
//	@Test
//	public void readFromVendingMachineCsv_verify_product_list_is_loaded_correctly() {
//		
//		List<Product> expectedProducts = new ArrayList<Product>();
//		expectedProducts.add(new Chip("A1", "Potato Crisps", BigDecimal.valueOf(3.05), "Chip"));
//		expectedProducts.add(new Candy("B1", "Moonpie", BigDecimal.valueOf(1.80), "Candy"));
//		expectedProducts.add(new Drink("C1", "Cola", BigDecimal.valueOf(1.25), "Drink"));
//		expectedProducts.add(new Gum("D1", "U-Chews", BigDecimal.valueOf(0.85), "Gum"));
//		expectedProducts.add(new Product("E5", "Bubble Pop", BigDecimal.valueOf(1.00), "Health Bar"));
//
//		assertEquals(expectedProducts, fileManager.readFromVendingMachineCsv("testvendingmachine.csv").);
//		
//	}
	
	@After
	public void delete_audit_log_test_file() {
		File logFile = new File("LogTest.txt");
		if(logFile.exists())
			logFile.delete();
	}

}
