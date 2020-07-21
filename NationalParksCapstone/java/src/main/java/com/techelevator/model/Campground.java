package com.techelevator.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Campground {
	private long campgroundId;
	private String name;
	private LocalDate openDate;
	private LocalDate closingDate;
	private BigDecimal dailyFee;
	private static final String CG_HEADER_FORMAT = "%4s %-34s%-12s%-12s%14s";
	private static final String CG_FORMAT = "%-34s%-12s%-12s%14s";
	private static final DecimalFormat DF = new DecimalFormat("$ #,##0.00");
	
	//Getters and Setters
	public BigDecimal getDailyFee() {
		return this.dailyFee;
	}
	
	public void setDailyFee(BigDecimal dailyFee) {
		this.dailyFee = dailyFee;
	}
	
	public long getCampgroundId() {
		return campgroundId;
	}
	public void setCampgroundId(long campgroundId) {
		this.campgroundId = campgroundId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getOpenDate() {
		return openDate;
	}
	public void setOpenDate(LocalDate openDate) {
		this.openDate = openDate;
	}
	public LocalDate getClosingDate() {
		return closingDate;
	}
	public void setClosingDate(LocalDate closingDate) {
		this.closingDate = closingDate;
	}
	
	public String toString() {
		String campgrounds = String.format(CG_FORMAT, this.getName(), 
				this.getOpenDate().getMonth().getDisplayName(TextStyle.FULL, Locale.US), 
				this.getClosingDate().getMonth().getDisplayName(TextStyle.FULL, Locale.US), 
				DF.format(this.getDailyFee()));
		return campgrounds;
	}
	
	public static String getHeader(String parkName) {
		String header = parkName + " National Park Campgrounds\n\n";
				header += String.format(CG_HEADER_FORMAT, "", "Name", "Open", "Close", "Daily Fee");
	return header;
	}
	
	
}
