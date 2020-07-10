package com.techelevator.view;

import com.techelevator.Product;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Scanner;

public class Menu {

	private PrintWriter out;
	private Scanner in;

	public Menu(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		return choice;
	}

	public Object getProductChoiceFromOptions(Object[] options, BigDecimal balance) {
		Object choice = null;
		while (choice == null) {
			displayProductMenuOptions(options, balance);
			choice = getProductChoiceFromUserInput(options);
		}
		return choice;
	}

	
	public Object getChoiceFromOptions(Object[] options, BigDecimal balance) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options, balance);
			choice = getChoiceFromUserInput(options);
		}
		return choice;
	}
	
	public BigDecimal feedMoney() {
		writeToConsoleLn("Please Enter One of the Following Dollar Amounts: 1, 2, 5, or 10");
		BigDecimal enteredMoney = new BigDecimal(0);
		try { 
			enteredMoney = BigDecimal.valueOf(Integer.parseInt(in.nextLine()));
		}
		catch (NumberFormatException e) {
			writeToConsoleLn("Invalid Amount: \nOnly Accepts One of the Following Dollar Amounts: 1, 2, 5, or 10");
	}
		return enteredMoney;
	}
	
	
	public void writeToConsoleLn(String message) {
		out.println();
		out.println(message);
		out.flush();
	}
	
	public void writeToConsole(String message) {
		out.println(message);
		out.flush();
	}
	
	private void displayProductMenuOptions(Object[] options, BigDecimal balance) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			
			Product option = (Product)options[i];
			if(option.getStockAmount() > 0)
			out.println(String.format("(%s) %-20s $%-7.2f", option.getProductLocation(), 
					option.getProductName(), option.getProductCost().setScale(2)));
		}
		out.println();
		out.println("Current Money Provided: $" + balance.setScale(2));
		out.print("\nPlease choose an option >>> ");
		out.flush();

	}

	private Object getProductChoiceFromUserInput(Object[] options) {
		String userInput = in.nextLine();
		Object choice = null;
		try {
			String selectedOption = (userInput);
			{
				for (Object option : options) {
					Product myOption = (Product) option;
					if (myOption.getProductLocation().equalsIgnoreCase(selectedOption) && myOption.getStockAmount() > 0) {
						choice = myOption;
					}
				}
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will
			// be null
		}
		if (choice == null) {
			writeToConsoleLn("\n*** " + userInput + " is not a valid option ***\n");
		}
		return choice;
	}


	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will
			// be null
		}
		if (choice == null) {
			writeToConsoleLn("\n*** " + userInput + " is not a valid option ***\n");
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		for (int i = 0; i < options.length-1; i++) {
			int optionNum = i + 1;
			out.println("(" + optionNum + ") " + options[i]);
		}
		out.print("\nPlease choose an option >>> ");
		out.flush();
	}

	private void displayMenuOptions(Object[] options, BigDecimal balance) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println("(" + optionNum + ") " + options[i]);
		}
		out.println();
		out.println("Current Money Provided: $" + balance.setScale(2));
		out.print("\nPlease choose an option >>> ");
		out.flush();
	}
}
