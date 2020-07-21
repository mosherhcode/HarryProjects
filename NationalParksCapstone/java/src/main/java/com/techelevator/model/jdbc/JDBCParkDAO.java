package com.techelevator.model.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Park;
import com.techelevator.model.ParkDAO;

public class JDBCParkDAO implements ParkDAO {

	private JdbcTemplate jdbcTemplate;
	
	public JDBCParkDAO(DataSource datasource) {
	
		jdbcTemplate = new JdbcTemplate(datasource);
	}

	@Override
	public List<Park> viewAllParks() {
		List<Park> parks = new ArrayList<Park>();
		
		String sqlViewAllParks = "SELECT * FROM park ORDER BY name ASC";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlViewAllParks);
		
		while (results.next()) {
			Park thePark = mapRowToPark(results.getLong("park_id"), 
					results.getString("name"), results.getString("location"), 
					results.getDate("establish_date").toLocalDate(), results.getLong("area"), 
					results.getLong("visitors"), results.getString("description"));
			parks.add(thePark);			
		}
		
		return parks;
	}

	private Park mapRowToPark(long parkId, String name, String location, LocalDate establishDate, long area, long visitorCount,
			String description) {
		Park thePark = new Park();
		thePark.setParkID(parkId);
		thePark.setName(name);
		thePark.setLocation(location);
		thePark.setEstablishedDate(establishDate);
		thePark.setArea(area);
		thePark.setVisitorsCount(visitorCount);
		thePark.setDescription(description);
		return thePark;
	}

}
