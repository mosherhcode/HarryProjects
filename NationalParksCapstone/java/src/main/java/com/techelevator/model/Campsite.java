package com.techelevator.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public class Campsite {

	private long campsiteId;
	private String campgroundName;
	private long campgroundID;
	private long campsiteNumber;
	private long maxOccupancy;
	private boolean isAccessible;
	private long maxRVLength;
	private boolean hasUtilities;
	private BigDecimal dailyFee;
	private static final String CS_HEADER_FORMAT = "%4s %-40s%12s%13s%16s%12s%12s%13s%15s";
	private static final String CS_FORMAT = "%-40s%12s%13s%16s%12s%12s%13s%15s";
	private static final String CS_RESERVATION_CONFIRMATION_HEADER_FORMAT = "%10s%12s%12s%16s%12s%13s%15s";
	private static final String CS_RESERVATION_CONFIRMATION_FORMAT = "%10s%12s%12s%16s%12s%13s%15s";
	private static final DecimalFormat DF = new DecimalFormat("$ #,##0.00");
	private Request userRequest;

	public long getCampsiteId() {
		return campsiteId;
	}
	public void setCampsiteId(long campsiteId) {
		this.campsiteId = campsiteId;
	}
	public String getCampgroundName() {
		return campgroundName;
	}
	public void setCampgroundName(String campgroundName) {
		this.campgroundName = campgroundName;
	}
	public long getCampgroundID() {
		return campgroundID;
	}
	public void setCampgroundID(long campgroundID) {
		this.campgroundID = campgroundID;
	}
	public long getCampsiteNumber() {
		return campsiteNumber;
	}
	public void setCampsiteNumber(long campsiteNumber) {
		this.campsiteNumber = campsiteNumber;
	}
	public long getMaxOccupancy() {
		return maxOccupancy;
	}
	public void setMaxOccupancy(long maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}
	public boolean isAccessible() {
		return isAccessible;
	}
	public void setAccessible(boolean isAccessible) {
		this.isAccessible = isAccessible;
	}
	public long getMaxRVLength() {
		return maxRVLength;
	}
	public void setMaxRVLength(long maxRVLength) {
		this.maxRVLength = maxRVLength;
	}
	public boolean hasUtilities() {
		return hasUtilities;
	}
	public void setHasUtilities(boolean hasUtilities) {
		this.hasUtilities = hasUtilities;
	}
	public BigDecimal getDailyFee() {
		return dailyFee;
	}
	public void setDailyFee(BigDecimal dailyFee) {
		this.dailyFee = dailyFee;
	}

	public Request getUserRequest() {
		return userRequest;
	}
	public void setUserRequest(Request userRequest) {
		this.userRequest = userRequest;
	}
	public static String getHeader() {
		return String.format(CS_HEADER_FORMAT, "", "Campground", "Accessible", "Max Occup.", "Max RV Length", "Utilities", "Site No.", "Daily Fee", "Total Stay");
	}
	
	public static String getCampsiteHeaderForConfirmation() {
		return String.format(CS_RESERVATION_CONFIRMATION_HEADER_FORMAT, "Accessible", "Max Occup.", "Max RV Length", "Utilities", "Site No.", "Daily Fee", "Total Stay");
	}
	public String getCampsiteDetailsForConfirmation() {
		String str = String.format(CS_RESERVATION_CONFIRMATION_FORMAT,
				(this.isAccessible)? "Yes   " : "No    ", Long.toString(this.maxOccupancy) + "     ", 
						(this.maxRVLength > 0)? Long.toString(this.maxRVLength) + "    " : "N/A     ", 
								(this.hasUtilities)? "Yes   " : "N/A   ", Long.toString(this.campsiteNumber) + "    ", DF.format(this.getDailyFee()), 
										DF.format(this.getDailyFee().multiply(BigDecimal.valueOf(ChronoUnit.DAYS.between(this.userRequest.getFromDate(), this.userRequest.getToDate())))));
		return str;
	}
	
	
	public String toString() {
		String str = String.format(CS_FORMAT, this.campgroundName,
				(this.isAccessible)? "Yes   " : "No    ", Long.toString(this.maxOccupancy) + "     ", 
						(this.maxRVLength > 0)? Long.toString(this.maxRVLength) + "    " : "N/A     ", 
								(this.hasUtilities)? "Yes   " : "N/A   ", 
										Long.toString(this.campsiteNumber) + "    ", 
										DF.format(this.getDailyFee()), 
										DF.format(this.getDailyFee().multiply(BigDecimal.valueOf(ChronoUnit.DAYS.between(this.userRequest.getFromDate(), this.userRequest.getToDate())))));
		return str;
	}
	

}