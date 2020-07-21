package com.techelevator.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Scanner;

import com.techelevator.model.Park;

public class Menu {
	
	private PrintWriter out;
	private Scanner in;

	public Menu(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options, String header, String footer, String numberFormat, boolean hasQuit) {
		Object choice = null;
		while(choice == null) {
			displayMenuOptions(options, header, footer, numberFormat, hasQuit);
			choice = getChoiceFromUserInput(options, hasQuit);
		}
		return choice;
	}
	
	private Object getChoiceFromUserInput(Object[] options, boolean hasQuit) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			if(hasQuit && userInput.equalsIgnoreCase("Q")) {
				choice = "Quit";
			}else {
				if(userInput.equals("0")) {
					choice = "Back";
				} else {
					int selectedOption = Integer.valueOf(userInput);
					if(selectedOption <= options.length) {
						choice = options[selectedOption - 1];
					}
				}
			}
		} catch(NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if(choice == null) {
			out.println("\n*** "+userInput+" is not a valid option ***\n");
		}
		return choice;
	}

	public void displayMenuOptions(Object[] options, String header, String footer, String numberFormat, boolean hasQuit) {
		out.println();
		
		if (header.length() > 0) {
			out.print(header+"\n");
		}

		for(int i = 0; i < options.length; i++) {
			int optionNum = i+1;
			out.printf(((numberFormat.length() > 0) ? numberFormat : "%s") + " %s\n", optionNum, options[i]);
			//%4s)
		}

		if (hasQuit) {
			out.printf(((numberFormat.length() > 0) ? numberFormat : "%s") + " %s\n", "Q", "Quit");
		}
		if(footer.length() > 0) {
			out.print("\n" + footer + " ");
		}
		out.flush();
	}
	

	public boolean getYTrueOrNFalseFromUser(String requestText) {
		while(true) {
			String userInput = getStringFromUser(requestText + "(Y or N)");
			if(userInput.equalsIgnoreCase("Y")) {
				return true;
			}
			if(userInput.equalsIgnoreCase("N")) {
				return false;
			}
			out.println("Invalid entry.\n");
			out.flush();
		}
	}
	
	public long getLongFromUser(String requestText) {
		while(true) {
			String userInput = getStringFromUser(requestText);
			try {
				long inputLong = Long.parseLong(userInput); 
				if(inputLong <= 0) {
					out.println("Please enter a positive number.\n");
					out.flush();
				}
				else {
					return inputLong;
				}
			} catch (NumberFormatException e) {
				out.println("Invalid number format. Please enter again.\n");
				out.flush();
			}
		}
	}

	public LocalDate[] getDateRangeFromUser(String startRequestString, String endRequestString) {
		
		while (true) {
			LocalDate startDate = getDateFromUser(startRequestString + " (in format MM/DD/YYYY)");
			LocalDate endDate = getDateFromUser(endRequestString + " (in format MM/DD/YYYY)");
			
			if (endDate.isAfter(startDate)) {
				LocalDate[] dateRange = {startDate, endDate};
				return dateRange;
			} else {
				out.print("Invalid Date Range\n");
				out.flush();
			}

		}
	}
	
	private LocalDate getDateFromUser(String dateRequestString) {
		while(true) {
			String userInput = getStringFromUser(dateRequestString);
			LocalDate theDate = LocalDate.now();

			// Lazy CLI Testing Method
			if (userInput.equals("6") == false && userInput.equals("7") == false) {  //LAZY METHOD
				try {
			
					String[] dateStringArray = userInput.split("/");
					theDate = LocalDate.of(Integer.parseInt(dateStringArray[2]), 
							Integer.parseInt(dateStringArray[0]), Integer.parseInt(dateStringArray[1]));
				

				if (theDate.isBefore(LocalDate.now())) {
					out.println("You entered a date in the past. No time traveling allowed.");
					out.flush();
					continue;
				}
				out.println("You entered " + theDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)) + "\n");
					return theDate;
				}catch(DateTimeException dte) {
					//Handles if they enter a date that doesn't fit the required format.
				}catch (NumberFormatException nfe) {
					//Handles if they entered something that wasn't a number for the date numbers.
				}catch(ArrayIndexOutOfBoundsException aioobe) {
					//Handles if they put in a date that doesn't have enough sections to it.
				}
					//If it gets here, an exception was caught.
					out.println("There was an issue with the date you entered.\n");
					out.flush();
				
			} else if (userInput.equals("6")) {   //LAZY METHOD
				theDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue() + 1, 5);
				out.println("You entered " + theDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)) + "\n");
				return theDate;//LAZY METHOD
			} else if (userInput.equals("7")) {
				theDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue() + 1, 12);
				out.println("You entered " + theDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)) + "\n");
				return theDate;//LAZY METHOD
			}  //LAZY METHOD
				

		}
	}
	
	
	public String getStringFromUser(String requestText) { //AKA the print out-house
		out.print(requestText + " ");
		out.flush();
		return in.nextLine();
	}
	
	public void printStringToConsoleLn(String stringToPrint) {
		out.println(stringToPrint);
		out.flush();
	}
	
	public void printSectionBreakToConsole() {
		out.print("\n\n--------------------------------------------------------------------------------\n\n");
		out.flush();
	}
	

}