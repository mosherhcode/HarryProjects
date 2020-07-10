package com.techelevator;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.techelevator.view.Menu;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String MAIN_MENU_OPTION_SALES_REPORT = "Sales Report";
	private static final String SUB_MENU_OPTION_FEED_MONEY = "Feed Money";
	private static final String SUB_MENU_OPTION_SELECT_PRODUCT = "Select Product";
	private static final String SUB_MENU_OPTION_FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE,
			MAIN_MENU_OPTION_EXIT, MAIN_MENU_OPTION_SALES_REPORT };
	private static final String[] SUB_MENU_OPTIONS = { SUB_MENU_OPTION_FEED_MONEY, SUB_MENU_OPTION_SELECT_PRODUCT,
			SUB_MENU_OPTION_FINISH_TRANSACTION };

	private Menu menu;

	// Create a new FileManager object to handle the reading and writing of files
	private FileManager fileManager = new FileManager();

	// Our list of products
	private List<Product> products = new ArrayList<>();

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;

		// Load in the product details from the input file.
		products = fileManager.readFromVendingMachineCsv("vendingmachine.csv");
		fileManager.setupAuditLog("Log.txt");
	}

	public void run() {
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				// Loops through our List of Product objects and prints out their details
				for (Product product : products) {
					menu.writeToConsole(product.toString());
				}
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				// We want a new customer each time we enter the Purchase menu
				Customer theCustomer = new Customer();
				while (true) {

					// Opens the Sub Menu and pass a balance
					String subChoice = (String) menu.getChoiceFromOptions(SUB_MENU_OPTIONS, theCustomer.getBalance());

					if (subChoice.contentEquals(SUB_MENU_OPTION_FEED_MONEY)) {
						// This will feed money into the Customer balance
						BigDecimal enteredMoney = menu.feedMoney();

						if (enteredMoney.compareTo(BigDecimal.valueOf(0)) == 0
								|| enteredMoney.compareTo(BigDecimal.valueOf(1)) == 0
								|| enteredMoney.compareTo(BigDecimal.valueOf(2)) == 0
								|| enteredMoney.compareTo(BigDecimal.valueOf(5)) == 0
								|| enteredMoney.compareTo(BigDecimal.valueOf(10)) == 0) {
							theCustomer.addToBalance(enteredMoney);
							fileManager.writeToLog(" " + "FEED MONEY:" + " $" + enteredMoney.setScale(2) + " $"
									+ theCustomer.getBalance().setScale(2));

						} else {
							menu.writeToConsoleLn(
									"Invalid Amount: \nOnly Accepts One of the Following Dollar Amounts: 1, 2, 5, or 10");
						}
					} else if (subChoice.contentEquals(SUB_MENU_OPTION_SELECT_PRODUCT)) {

						// Send in the productList as the options for the menu
						Product productChoice = (Product) menu.getProductChoiceFromOptions(products.toArray(),
								theCustomer.getBalance());

						// If the customer has enough money and the product is in stock, the complete
						// the purchase.
						if (productChoice.getProductCost().compareTo(theCustomer.getBalance()) <= 0
								&& productChoice.getStockAmount() > 0) {

							fileManager.writeToLog(" " + productChoice.getProductName() + " "
									+ productChoice.getProductLocation() + " $" + theCustomer.getBalance().setScale(2)
									+ " $"
									+ (theCustomer.getBalance().subtract(productChoice.getProductCost())).setScale(2));

							theCustomer.subtractFromBalance(productChoice.getProductCost());
							productChoice.setStockAmount(productChoice.getStockAmount() - 1);
							menu.writeToConsoleLn(productChoice.getMessage());

							// When the customer doesn't have enough money, inform them and cancel the
							// purchase.
						} else if (productChoice.getProductCost().compareTo(theCustomer.getBalance()) > 0) {
							menu.writeToConsoleLn("Please enter more money.");

							// When the product is out of stock inform the customer and cancel the purchase.
						} else if (productChoice.getStockAmount() <= 0) {
							menu.writeToConsoleLn("Out of Stock");
						}
					}

					else if (subChoice.contentEquals(SUB_MENU_OPTION_FINISH_TRANSACTION)) {
						// Write the amount of change returned into the customer

						fileManager.writeToLog(
								" " + "GIVE CHANGE:" + " $" + theCustomer.getBalance().setScale(2) + " $" + "0.00");

						menu.writeToConsoleLn(theCustomer.returnChange());

						// break will return us to the Main Menu
						break;
					}
				}

			} else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {
				System.exit(1);
			} else if (choice.contentEquals(MAIN_MENU_OPTION_SALES_REPORT)) {
				try {
					String salesReportName = fileManager.generateToSalesReport();
					menu.writeToConsoleLn("Sales Report \""+ salesReportName  + "\" Has Been Generated");
				} catch (FileNotFoundException e) {
					menu.writeToConsoleLn("Sales Report Data Not Found");
				}
			}
		}
	}

	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();
	}
}
