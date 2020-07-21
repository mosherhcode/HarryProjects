package com.techelevator.model;

import java.util.List;

public interface ReservationDAO {

	public Reservation addReservation(Request request);
	
	public boolean cancelReservation(Long reservationId);
	
	public List<Reservation> getAllReservationsInParkforNext30Days (Park park);
	
}
