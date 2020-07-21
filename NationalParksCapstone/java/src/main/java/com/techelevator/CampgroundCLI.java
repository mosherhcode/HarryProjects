package com.techelevator;

import java.time.LocalDate;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.model.Campground;
import com.techelevator.model.CampgroundDAO;
import com.techelevator.model.Campsite;
import com.techelevator.model.CampsiteDAO;
import com.techelevator.model.Park;
import com.techelevator.model.ParkDAO;
import com.techelevator.model.Request;
import com.techelevator.model.Reservation;
import com.techelevator.model.ReservationDAO;
import com.techelevator.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.model.jdbc.JDBCCampsiteDAO;
import com.techelevator.model.jdbc.JDBCParkDAO;
import com.techelevator.model.jdbc.JDBCReservationDAO;
import com.techelevator.view.ConfirmationPrinter;
import com.techelevator.view.Menu;
import com.techelevator.view.MenuOption;

public class CampgroundCLI {

	private static final MenuOption[] PARK_MENU_OPTIONS = { MenuOption.PARK_MENU_OPTION_VIEW_CAMPGROUNDS,
			MenuOption.PARK_MENU_OPTION_SEARCH_RESERVATION, MenuOption.PARK_MENU_OPTION_UPCOMING_RESERVATION, MenuOption.PARK_MENU_OPTION_RETURN_PREVIOUS };
	

	private ParkDAO pDao;
	private CampgroundDAO cgDao;
	private CampsiteDAO csDao;
	private ReservationDAO rDao;
	private Menu menu;
	private Request userRequest;

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource datasource) {
		pDao = new JDBCParkDAO(datasource);
		cgDao = new JDBCCampgroundDAO(datasource);
		csDao = new JDBCCampsiteDAO(datasource);
		rDao = new JDBCReservationDAO(datasource);
		menu = new Menu(System.in, System.out);
		userRequest = new Request();
	}

	public void run() {
		while (true) {
			
			displayApplicationBanner();
			Object choice = menu.getChoiceFromOptions(pDao.viewAllParks().toArray(), Park.getHeader(), "", "%3s)", true);
			menu.printSectionBreakToConsole();
			if (choice.equals("Quit")) {
				menu.printStringToConsoleLn("Thank you for considering The National Park Service for your recreational needs.  Have a great day!");
				System.exit(0);
			} else if (choice != null) {
				// view park sub menu
				useParkSubMenu(choice);
				continue;
				

			}

		}

	}

	private void useParkSubMenu(Object choice) {
		
		//Set the selected park in the request object
		userRequest.setDesiredPark((Park) choice);

		while (true) {
			menu.printStringToConsoleLn(userRequest.getDesiredPark().getInfoString());

			choice = menu.getChoiceFromOptions(PARK_MENU_OPTIONS, "Select a Command", "", "%3s)", false);
			menu.printSectionBreakToConsole();

			if (choice.equals(MenuOption.PARK_MENU_OPTION_VIEW_CAMPGROUNDS)) {

				if (useCampgroundSubMenu() == false) {
					continue;
				} else {
					return;
				}
					
			} else if (choice.equals(MenuOption.PARK_MENU_OPTION_SEARCH_RESERVATION)) {
				if (haveUserCompleteRequestforReservation() == false) {
					continue;
				} else {
					return;
				}

			} else if(choice.equals(MenuOption.PARK_MENU_OPTION_UPCOMING_RESERVATION)) {
				menu.displayMenuOptions(rDao.getAllReservationsInParkforNext30Days(userRequest.getDesiredPark()).toArray(), 
						userRequest.getDesiredPark().getName() + " reservations over the next 30 days \n" + Reservation.getHeader(), "", "%3s)", false);	
				menu.getStringFromUser("Press Enter to continue.");
				menu.printSectionBreakToConsole();
			} else if (choice.equals(MenuOption.PARK_MENU_OPTION_RETURN_PREVIOUS)) {
				return;
			}
		}
	}


	private boolean useCampgroundSubMenu() {
		while (true) {
			
			Object choice = menu.getChoiceFromOptions(cgDao.viewAllCampgroundsInPark(userRequest).toArray(),
					Campground.getHeader(userRequest.getDesiredPark().getName()), "Select the campground that you would like to visit. (enter 0 to cancel)", 
					"%3s)", false);

			if (choice.equals("Back")) {
				return false;
			}
			userRequest.setDesiredCampground((Campground)choice);
			
			if (haveUserCompleteRequestforReservation() == false) {
				continue;
			} else {
				return true;
			}
			
		}

	}
	
	private boolean haveUserCompleteRequestforReservation() {
		while (true) {
			LocalDate[] dateRange = menu.getDateRangeFromUser(
					MenuOption.CAMPSITE_START_DATE_REQUEST_TEXT.toString(),
					MenuOption.CAMPSITE_END_DATE_REQUEST_TEXT.toString());
			
			userRequest.setFromDate(dateRange[0]);
			userRequest.setToDate(dateRange[1]);

			userRequest.setPartySize(menu.getLongFromUser(MenuOption.CAMPSITE_PARTY_SIZE_REQUEST_TEXT.toString()));
			
			if (menu.getYTrueOrNFalseFromUser(MenuOption.CAMPSITE_RV_REQUEST_TEXT.toString())) {
				userRequest.setRvLength(
						menu.getLongFromUser(MenuOption.CAMPSITE_RV_LENGTH_REQUEST_TEXT.toString()));
				userRequest.setNeedsUtilities(
						menu.getYTrueOrNFalseFromUser(MenuOption.CAMPSITE_UTILITIES_REQUEST_TEXT.toString()));
			}
			userRequest.setNeedsAccessible(
					menu.getYTrueOrNFalseFromUser(MenuOption.CAMPSITE_ACCESSIBILITY_REQUEST_TEXT.toString()));

			menu.printSectionBreakToConsole();

			Object[] availableCampsites = csDao.viewAvailableCampsitesFromRequest(userRequest).toArray();
			if (availableCampsites.length > 0) {
				Object choice = menu.getChoiceFromOptions(availableCampsites,
						"Results Matching Your Search Criteria\n\n" + Campsite.getHeader(),
						"Which site should be reserved (enter 0 to cancel)?", "%3s)", false);
				
				if (choice.equals("Back")) {
					menu.printSectionBreakToConsole();
					return false;
				}
				userRequest.setDesiredCampsite((Campsite)choice);
				if (userRequest.getDesiredCampground() == null) {
					userRequest.setDesiredCampground(cgDao.getCampgroundFromId(userRequest.getDesiredCampsite().getCampgroundID()));
				}
				
				userRequest.setPartyName(menu.getStringFromUser(MenuOption.RESERVATION_NAME_REQUEST_TEXT.toString()));
				
				menu.printStringToConsoleLn(userRequest.getReservationConfirmationRequestDetails());
				if(menu.getYTrueOrNFalseFromUser("Please confirm whether the details above are correct.")) {
					
					userRequest.setFinalReservation(rDao.addReservation(userRequest));
					menu.printSectionBreakToConsole();
					menu.printStringToConsoleLn(userRequest.getFinalizedReservationConfirmation());
					ConfirmationPrinter.printReservation("UserReceipt.html", userRequest.getFinalizedReservationConfirmation());
					menu.getStringFromUser("Press Enter to continue.");
					menu.printSectionBreakToConsole();
					return true;
				
				} else {
					menu.printSectionBreakToConsole();
					return false;
				}
				
			} else {
				
				menu.getStringFromUser("\nYour search criteria didn't return any results. Try entering different criteria.\nPress Enter to continue.");
				menu.printSectionBreakToConsole();

				return false;
			}
			
			}
		}
	

	private void displayApplicationBanner() {
		menu.printStringToConsoleLn("National Park Service Camping Reservations\n" +
				"                                               _\r\n" + 
				"                 ___                          (_)\r\n" + 
				"               _/XXX\\\r\n" + 
				"_             /XXXXXX\\_                                    __\r\n" + 
				"X\\__    __   /X XXXX XX\\                          _       /XX\\__      ___\r\n" + 
				"    \\__/  \\_/__       \\ \\                       _/X\\__   /XX XXX\\____/XXX\\\r\n" + 
				"  \\  ___   \\/  \\_      \\ \\               __   _/      \\_/  _/  -   __  -  \\__/\r\n" + 
				" ___/   \\__/   \\ \\__     \\\\__           /  \\_//  _ _ \\  \\     __  /  \\____//\r\n" + 
				"/  __    \\  /     \\ \\_   _//_\\___     _/    //           \\___/  \\/     __/\r\n" + 
				"__/_______\\________\\__\\_/________\\_ _/_____/_____________/_______\\____/_______\r\n" + 
				"                                  /|\\\r\n" + 
				"                                 / | \\\r\n" + 
				"                                /  |  \\\r\n" + 
				"                               /   |   \\                     Brought to you by\r\n" + 
				"                              /    |    \\                        Ben and Harry\r\n");

		
		
		
	}
}
