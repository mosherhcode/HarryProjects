package com.techelevator.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Request {

	private Park desiredPark;
	private Campground desiredCampground;
	private Campsite desiredCampsite;
	private Reservation finalReservation;
		
	private LocalDate fromDate;
	private LocalDate toDate;
	private String partyName;
	private long rvLength;
	private boolean needsAccessible;
	private boolean needsUtilities;
	private long partySize;
	
	//VARIABLES AND GETTER/SETTER TO ALLOW US TO REMOVE LIMIT STATEMENTS FROM SQL FOR PURPOSE OF TESTING.
	private boolean isTesting;
	
	public boolean isTesting() {
		return isTesting;
	}
	public void setTesting(boolean isTesting) {
		this.isTesting = isTesting;
	}
	
	
	//REGULAR GETTERS AND SETTERS
	public Park getDesiredPark() {
		return desiredPark;
	}
	public void setDesiredPark(Park desiredPark) {
		this.desiredPark = desiredPark;
	}
	public Campground getDesiredCampground() {
		return desiredCampground;
	}
	public void setDesiredCampground(Campground desiredCampground) {
		this.desiredCampground = desiredCampground;
	}
	public Campsite getDesiredCampsite() {
		return desiredCampsite;
	}
	public void setDesiredCampsite(Campsite desiredCampsite) {
		this.desiredCampsite = desiredCampsite;
	}
	public LocalDate getFromDate() {
		return fromDate;
	}
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}
	public LocalDate getToDate() {
		return toDate;
	}
	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}
	public long getRvLength() {
		return rvLength;
	}
	public void setRvLength(long rvLength) {
		this.rvLength = rvLength;
	}
	public boolean needsAccessible() {
		return needsAccessible;
	}
	public void setNeedsAccessible(boolean needsAccessible) {
		this.needsAccessible = needsAccessible;
	}
	public boolean needsUtilities() {
		return needsUtilities;
	}
	public void setNeedsUtilities(boolean needsUtilities) {
		this.needsUtilities = needsUtilities;
	}
	public long getPartySize() {
		return partySize;
	}
	public void setPartySize(long partySize) {
		this.partySize = partySize;
	}
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	public Reservation getFinalReservation() {
		return finalReservation;
	}
	public void setFinalReservation(Reservation finalReservation) {
		this.finalReservation = finalReservation;
	}
	
	
	//ADDITIONAL METHODS

	public String getReservationConfirmationRequestDetails () {
		String reservation = String.format("\n"
				+ "%s, Please confirm your Reservation Deals:\n"
				+ "%s National Park\n"
				+ "%-20s %s\n"
				+ "%-20s %s\n"
				+ "\n"
				+ "%-20s %s %s\n"
				+ "%-20s %s\n"
				+ "%-20s %s\n"
				+ "\n"
				+ "%-20s %s\n"
				+ "%-20s %s\n"
				+ "%-20s %s\n"
				+ "%-20s %s\n"
				+ "\n"
				+ "%s\n"
				+ "%s\n"
				+ "\n", 
				getPartyName(),
				getDesiredPark().getName(),
				"Location:", getDesiredPark().getLocation(), 
				"Campground:", getDesiredCampground().getName(),
				
				"Length of Stay:", Long.valueOf(ChronoUnit.DAYS.between(getFromDate(), getToDate())), "Nights",
				"Check-In Date:", getFromDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
				"Check-Out Date:", getToDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
				
				"Party Size:", getPartySize(),
				"Needs Accessibility:", (this.needsAccessible)? "Yes" : "No", 
				"RV Length:", (this.rvLength > 0)? Long.toString(this.rvLength) : "N/A", 
				"Needs Utilities:", (this.needsUtilities())? "Yes" : "N/A",
				Campsite.getCampsiteHeaderForConfirmation(),
				this.desiredCampsite.getCampsiteDetailsForConfirmation()); 
		return reservation;
	}
	
	public String getFinalizedReservationConfirmation () {
		String reservation = String.format("\n"
				+ "Thank you so much for confirming your reservation, we sincerely hope you enjoy your stay!\n"
				+ "\n"
				+ "%-20s %s\n"
				+ "%-20s %s\n"
				+ "%-20s %s\n"
				+ "\n"
				+ "%s National Park\n"
				+ "%-20s %s\n"
				+ "%-20s %s\n"
				+ "\n"
				+ "%-20s %s %s\n"
				+ "%-20s %s\n"
				+ "%-20s %s\n"
				+ "\n"
				+ "%-20s %s\n"
				+ "%-20s %s\n"
				+ "%-20s %s\n"
				+ "%-20s %s\n"
				+ "\n"
				+ "%s\n"
				+ "%s\n"
				+ "\n",
				
				"Reservation Created:", this.finalReservation.getCreatedDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
				"Reservation ID:", this.finalReservation.getReservationId(),
				"Party Name:", getPartyName(),
				
				getDesiredPark().getName(),
				"Location:", getDesiredPark().getLocation(), 
				"Campground:", getDesiredCampground().getName(),
				
				"Length of Stay:", Long.valueOf(ChronoUnit.DAYS.between(getFromDate(), getToDate())), "Nights",
				"Check-In Date:", getFromDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
				"Check-Out Date:", getToDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
				
				"Party Size:", getPartySize(),
				"Needs Accessibility:", (this.needsAccessible)? "Yes" : "No", 
				"RV Length:", (this.rvLength > 0)? Long.toString(this.rvLength) : "N/A", 
				"Needs Utilities:", (this.needsUtilities())? "Yes" : "N/A",
				Campsite.getCampsiteHeaderForConfirmation(),
				this.desiredCampsite.getCampsiteDetailsForConfirmation()); 
		return reservation;
	}
	
	
	
}
