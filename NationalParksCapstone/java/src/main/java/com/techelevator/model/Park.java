package com.techelevator.model;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.techelevator.view.WordWrapper;

public class Park {
	private long parkID;
	private String name;
	private String location;
	private LocalDate establishedDate;
	private long area;
	private long visitorsCount;
	private String description;
	
	public long getParkID() {
		return parkID;
	}
	public void setParkID(long parkID) {
		this.parkID = parkID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public LocalDate getEstablishedDate() {
		return establishedDate;
	}
	public void setEstablishedDate(LocalDate establishedDate) {
		this.establishedDate = establishedDate;
	}
	public long getArea() {
		return area;
	}
	public void setArea(long area) {
		this.area = area;
	}
	public long getVisitorsCount() {
		return visitorsCount;
	}
	public void setVisitorsCount(long visitorsCount) {
		this.visitorsCount = visitorsCount;
	}
	public String getDescription() {
		return WordWrapper.wrapMyString(description);
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String toString() {
		return this.getName();
	}
	
	public String getInfoString() {
		String parkInfoString = "";
		parkInfoString = String.format("\n%s National Park\n\n"
				+ "%-17s %s \n"
				+ "%-17s %s \n"
				+ "%-17s %s \n"
				+ "%-17s %s \n"
				+ "\n"
				+ "%s\n", name, "Location:", location, "Established:", 
				establishedDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")), 
				"Area:", NumberFormat.getIntegerInstance().format(area) + " sq km", 
				"Annual Visitors:",	NumberFormat.getIntegerInstance().format(visitorsCount), 
				WordWrapper.wrapMyString(description)); 
		return parkInfoString;
	}
	
	public static String getHeader() {
		return "Select a Park for Further Details";
	}

	
}
