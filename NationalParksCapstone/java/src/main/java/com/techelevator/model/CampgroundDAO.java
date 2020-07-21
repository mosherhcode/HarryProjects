package com.techelevator.model;

import java.util.List;

public interface CampgroundDAO {

	public List<Campground> viewAllCampgroundsInPark (Request request);
	
	public Campground getCampgroundFromId(long campgroundId);
	
}
 