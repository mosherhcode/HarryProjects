package com.techelevator.model.jdbc;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Campsite;
import com.techelevator.model.Request;

public class JDBCCampsiteDAO implements com.techelevator.model.CampsiteDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCCampsiteDAO(DataSource datasource) {
		jdbcTemplate = new JdbcTemplate(datasource);
	}

/* Behold the beauty of our spaceship method. A bit of curiosity, okay, maybe tons
 * of it has lead us both down some pretty crazy rabbit holes but this is probably
 * the deepest one.
 *    
 *                      `. ___
                    __,' __`.                _..----....____
        __...--.'``;.   ,.   ;``--..__     .'    ,-._    _.-'
  _..-''-------'   `'   `'   `'     O ``-''._   (,;') _,'
,'________________                          \`-._`-','
 `._              ```````````------...___   '-.._'-:
    ```--.._      ,.                     ````--...__\-.
            `.--. `-`                       ____    |  |`
              `. `.                       ,'`````.  ;  ;`
                `._`.        __________   `.      \'__/`
                   `-:._____/______/___/____`.     \  `
                               |       `._    `.    \
                               `._________`-.   `.   `.___
                                                  `------'`		*/
	
	@Override
	public List<Campsite> viewAvailableCampsitesFromRequest(Request request) { 
		List<Campsite> campsites = new ArrayList<Campsite>();
		List<Object> arguments = new ArrayList<Object>(0); //Default build of list is 10, needed to start with size 0.
		List<Long> campgroundIDs = new ArrayList<Long>();

		//If campground is chosen, override the camproundIDs array to allow the for loop to use single campground.
		if (request.getDesiredCampground() != null) {
			campgroundIDs.add(request.getDesiredCampground().getCampgroundId());
		}

		if (request.getDesiredCampground() == null && request.getDesiredPark() != null) {
			//If no campground is chosen, but a park is, pull all campground_id's for a park and save them as campgroundIDs list.
			campgroundIDs = getCampgroundListFromWithinAPark(request.getDesiredPark().getParkID());
		}

		//Loop through campground IDs of Park if no campground had been chosen yet.  Otherwise campground IDs list will only have the chosen campground. 
		for (int i = 0; i < campgroundIDs.size(); i++) {

			//Begin Building of String for Query / Also begin building of argument array when required.
			String sqlViewAllCampsitesInCampground = "SELECT s.*, c.name, c.daily_fee "
													+ "FROM site s "
													+ "INNER JOIN campground c ON s.campground_id = c.campground_id "
													+ "WHERE ";

			sqlViewAllCampsitesInCampground += "s.campground_id = ? AND ";
			arguments.add(campgroundIDs.get(i));
				
		    //Menu handles values of 0 or less as invalid.
			sqlViewAllCampsitesInCampground += "s.max_occupancy >= ? AND ";
			arguments.add(request.getPartySize());

			if(request.getRvLength() > 0) {
				sqlViewAllCampsitesInCampground += "s.max_rv_length >= ? AND ";
				arguments.add(request.getRvLength());
			}  //No false argument required in case that they didn't have an RV.  It'd be a site bonus to user.
			if(request.needsUtilities()) {
				sqlViewAllCampsitesInCampground += "s.utilities IS TRUE AND ";
			}  //No false argument required in case that they don't need utilities.  It'd be a site bonus to user.
			if(request.needsAccessible()) {
				sqlViewAllCampsitesInCampground += "s.accessible IS TRUE AND ";
			}  //No false argument required in case that they don't need accessibility.  It'd be a site bonus to user.

			sqlViewAllCampsitesInCampground += "((c.open_from_mm::integer <= ?) AND "
												+ "(c.open_to_mm::integer >= ?)) AND "
												+ "s.site_id NOT IN "
												+ "(SELECT site_id "
												+ "FROM reservation "
												+ "WHERE "
												+ "(from_date <= ? AND to_date >= ?) "
												+ "OR "
												+ "(from_date <= ? AND to_date >= ?)) "
												+ "GROUP BY s.campground_id, s.site_id, c.name, c.daily_fee "
												+ "ORDER BY c.daily_fee ASC ";  // Always feed cheapest sites available regardless of options chosen.
			if(request.isTesting() == true) {
				//Don't Add Limits to Result Set
			} else {
				//If not testing Add Limits to Result Set
				sqlViewAllCampsitesInCampground += "LIMIT 5 ";
			}
			
			LocalDate fromDate = request.getFromDate();
			LocalDate toDate = request.getToDate();
			arguments.add(fromDate.getMonthValue());
			arguments.add(toDate.getMonthValue());
			arguments.add(Date.valueOf(fromDate));
			arguments.add(Date.valueOf(fromDate));
			arguments.add(Date.valueOf(toDate));
			arguments.add(Date.valueOf(toDate));
			
			SqlRowSet results = jdbcTemplate.queryForRowSet(sqlViewAllCampsitesInCampground, arguments.toArray());
	
			while (results.next()) {
				Campsite theCampsite = mapRowToCampsite(results.getLong("site_id"), results.getString("name"), results.getLong("campground_id"), 
						results.getLong("site_number"), results.getLong("max_occupancy"),
						results.getBoolean("accessible"), results.getLong("max_rv_length"),results.getBoolean("utilities"), results.getBigDecimal("daily_fee"), request);
				campsites.add(theCampsite);
			}
			
			//If testing, do not loop for additional results.
			if (request.isTesting() == true) {
				break;
			}
			
			//Clear arguments List for subsequent loops.
			arguments.removeAll(arguments);
		}

		return campsites;
	}
	
	private List<Long> getCampgroundListFromWithinAPark(Long parkId) {
		List<Long> campgroundIDs = new ArrayList<Long>();
		String sqlGetCampgroundIDsWithinAPark = "SELECT c.campground_id " 
												+ "FROM campground c " 
												+ "WHERE "
												+ "c.park_id = ? " 
												+ "GROUP BY c.campground_id ";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetCampgroundIDsWithinAPark, parkId);

		while (results.next()) {
			campgroundIDs.add(results.getLong("campground_id"));
		}
		return campgroundIDs;
	}
	
	private Campsite mapRowToCampsite (Long campsiteId, String campgroundName, Long campgroundId, Long campsiteNumber, Long maxOccupancy, boolean isAccessible, long maxRVLength, boolean hasUtilities, BigDecimal dailyFee, Request userRequest) {
		Campsite theCampsite = new Campsite();
		theCampsite.setCampsiteId(campsiteId);
		theCampsite.setCampgroundName(campgroundName);
		theCampsite.setCampgroundID(campgroundId);
		theCampsite.setCampsiteNumber(campsiteNumber);
		theCampsite.setMaxOccupancy(maxOccupancy);
		theCampsite.setAccessible(isAccessible);
		theCampsite.setMaxRVLength(maxRVLength);
		theCampsite.setHasUtilities(hasUtilities);
		theCampsite.setDailyFee(dailyFee);
		theCampsite.setUserRequest(userRequest);
		return theCampsite;
	}
	
	

}
