package com.techelevator.model;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Reservation {

	private long reservationId;
	private long siteId;
	private String name;
	private LocalDate fromDate;
	private LocalDate toDate;
	private LocalDate createdDate;

	private static final String R_HEADER_FORMAT = "%4s %-14s%-14s%8s   %-40.38s%18s%14s";
	private static final String R_FORMAT =            "%-14s%-14s%8s   %-40.38s%18s%14s";
	private static final DecimalFormat DF = new DecimalFormat("$ #,##0.00");
	
	
	public long getReservationId() {
		return reservationId;
	}
	public void setReservationId(long reservationId) {
		this.reservationId = reservationId;
	}
	public long getSiteId() {
		return siteId;
	}
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public LocalDate getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}
	

	
	public static String getHeader() {
		String header = String.format(R_HEADER_FORMAT, "", "Arrival", "Departure", "Site ID", "Party Name", "Reservation ID", "Created");
		return header;
	}
	
	public String toString() {
		String reservations = String.format(R_FORMAT, 
				getFromDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")), 
				getToDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
				getSiteId() + "   ", getName(), getReservationId(), 
				getCreatedDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
		return reservations;
	}
	
}
