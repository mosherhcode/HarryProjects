package com.techelevator.model;

import java.util.List;

public interface CampsiteDAO {
	
	public List<Campsite> viewAvailableCampsitesFromRequest (Request request);
	
}
