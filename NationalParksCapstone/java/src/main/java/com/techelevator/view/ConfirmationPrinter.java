package com.techelevator.view;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import com.techelevator.model.Request;

public class ConfirmationPrinter {
	
	
		public static boolean printReservation(String path, String data) {
			
	        File receiptFile = new File(path);
	    	try(PrintWriter writer = new PrintWriter(receiptFile)){
	    		writer.println("<html>");
	    		writer.println("<body>");
	    		
	    		writer.println("<img src=\"src/main/resources/NationalParkService.jpg\" width=\"650px\" >");

	    		writer.println("<pre>" + data + "</pre>");
	    		
	    		writer.println("</body>");
	    		writer.println("</html>");
	    		writer.flush();
	            Desktop.getDesktop().open(receiptFile);
	        }
	        catch(IOException e) {
	        	System.err.println("Issue opening receipt file.");
	        }
	    	
	    	return true;
	        
	    }
	}
	
