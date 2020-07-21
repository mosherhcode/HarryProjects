package com.techelevator.view;

public enum MenuOption {
	PARK_MENU_OPTION_VIEW_CAMPGROUNDS("View Campgrounds"),
	PARK_MENU_OPTION_SEARCH_RESERVATION ("Search for Reservation"),
	PARK_MENU_OPTION_UPCOMING_RESERVATION("View Reservations Over the Next 30 Days"),
	PARK_MENU_OPTION_RETURN_PREVIOUS ("Return to Previous Screen"),
	CAMPSITE_START_DATE_REQUEST_TEXT ("What is your party's arrival date?"),
	CAMPSITE_END_DATE_REQUEST_TEXT("What is your party's departure date?"),
	CAMPSITE_PARTY_SIZE_REQUEST_TEXT ("How many people will be in your party?"),
	CAMPSITE_UTILITIES_REQUEST_TEXT ("Do you need a utility hookup?"),
	CAMPSITE_ACCESSIBILITY_REQUEST_TEXT("Do you need an accessible campsite?"),
	CAMPSITE_RV_REQUEST_TEXT ("Does the campsite need to accomodate an RV?"),
	CAMPSITE_RV_LENGTH_REQUEST_TEXT ("How long is your RV in feet?"),
	RESERVATION_NAME_REQUEST_TEXT ("What name should the reservation be made under?");
	

	private String option;
	
	public String[] getParkMenuOptionStrings() {
		String[] options = {PARK_MENU_OPTION_VIEW_CAMPGROUNDS.getOption(), PARK_MENU_OPTION_SEARCH_RESERVATION.getOption()};
		return options;
	}
	
	public MenuOption[] getParkMenuOptions() {
		MenuOption[] options = {PARK_MENU_OPTION_VIEW_CAMPGROUNDS, PARK_MENU_OPTION_SEARCH_RESERVATION, 
				PARK_MENU_OPTION_RETURN_PREVIOUS};
		return options;
	}
	
	public String getOption() {
		return this.option;
	}
	
	private MenuOption(String option) {
		this.option = option;
	}
	
	@Override
	public String toString() {
		return this.getOption();
	}
}
