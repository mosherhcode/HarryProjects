package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class FileManager {

	private File logFile;
	private File inputCsvFile;
	// private PrintWriter writer;
	private Scanner scanner;

	public FileManager() {
	}

	// methods
	public void writeToLog(String message) {
		try (PrintWriter writer = new PrintWriter(new FileOutputStream(logFile, true))) {
			writer.println(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) + " "
					+ LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a")) + message);
			writer.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String generateToSalesReport() throws FileNotFoundException {
		Map<String, Integer> productMap = new HashMap<>();
		

		//This try with resources is to prime the productMap with the default product names.
		//Allows us to show products in the sales report even when no purchase have been made for it.
		try (Scanner csvFileScanner = new Scanner(inputCsvFile)) {
			while (csvFileScanner.hasNextLine()) {
				String line = csvFileScanner.nextLine();
				String[] productDataArray = line.split("\\|");
				
				productMap.put(productDataArray[1].trim(), 0);
			}
		}
		
		try (Scanner logFileScanner = new Scanner(logFile)) {
			BigDecimal salesTotal = new BigDecimal(0);
			
			
			
			while (logFileScanner.hasNextLine()) {
				String line = logFileScanner.nextLine();
				String[] salesReportArray = line.split(" ");
				
				
				if (!salesReportArray[salesReportArray.length - 3].endsWith(":") 
						&& !salesReportArray[salesReportArray.length - 1].endsWith("**")) {
					BigDecimal balanceBeforePurchase = BigDecimal.valueOf(Double.parseDouble(salesReportArray[salesReportArray.length - 2].substring(1)));
					BigDecimal balanceAfterPurchase = BigDecimal.valueOf(Double.parseDouble(salesReportArray[salesReportArray.length - 1].substring(1)));
					
					salesTotal = salesTotal.add(balanceBeforePurchase.subtract(balanceAfterPurchase));
					
					//Get the name of the product
					String productName = "";
					for(int i = 3; i < salesReportArray.length - 3; i ++) {
						productName += salesReportArray[i] + " ";
					}
					//Get rid of the trailing spaces
					productName = productName.trim();
						
					//update the productMap with the total number of purchases of the product
					if(productMap.containsKey(productName)) {
						productMap.put(productName, productMap.get(productName) + 1);
					} else {
						productMap.put(productName, 1);
					}
				}
			}
			
			//Write it to the file
			String salesReportFileName = "SalesReport" + LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")) + "-"
					+ LocalTime.now().format(DateTimeFormatter.ofPattern("hh-mm-ss-a")) + ".txt";
			File salesReportFile = new File(salesReportFileName);
			try(PrintWriter writer = new PrintWriter(salesReportFile)){
				for(Entry<String, Integer> product : productMap.entrySet()) {
					writer.println(product.getKey() + "|" + product.getValue());
				}
				writer.println();
				writer.println("TOTAL SALES: $" + salesTotal.setScale(2));
			}
			return salesReportFileName;
		}
		

	}

	public void setupAuditLog(String path) {
		// create the file
		logFile = new File(path);

		// create the printwriter with the logFile object
		try (PrintWriter writer = new PrintWriter(new FileOutputStream(logFile, true))) {
			// Write a starting line to the Audit Log
			writer.println("**Starting Vending Machine Audit Log: "
					+ LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) + " "
					+ LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a")) + "**");
			writer.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public List<Product> readFromVendingMachineCsv(String path) {
		List<Product> products = new ArrayList<Product>();

		inputCsvFile = new File(path);

		try (Scanner fileScanner = new Scanner(inputCsvFile)) {
			while (fileScanner.hasNextLine()) {
				String line = fileScanner.nextLine();
				String[] productDataArray = line.split("\\|");
				if (productDataArray[3].equals("Chip")) {
					products.add(new Chip(productDataArray[0], productDataArray[1],
							BigDecimal.valueOf(Double.parseDouble(productDataArray[2])), productDataArray[3]));
				} else if (productDataArray[3].equals("Candy")) {
					products.add(new Candy(productDataArray[0], productDataArray[1],
							BigDecimal.valueOf(Double.parseDouble(productDataArray[2])), productDataArray[3]));
				} else if (productDataArray[3].equals("Drink")) {
					products.add(new Drink(productDataArray[0], productDataArray[1],
							BigDecimal.valueOf(Double.parseDouble(productDataArray[2])), productDataArray[3]));
				} else if (productDataArray[3].equals("Gum")) {
					products.add(new Gum(productDataArray[0], productDataArray[1],
							BigDecimal.valueOf(Double.parseDouble(productDataArray[2])), productDataArray[3]));
				} else {
					// Allows the application to handle items other than the main 4 types.
					products.add(new Product(productDataArray[0], productDataArray[1],
							BigDecimal.valueOf(Double.parseDouble(productDataArray[2])), productDataArray[3]));
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(path + " does not exist");
		}
		return products;
	}
}
