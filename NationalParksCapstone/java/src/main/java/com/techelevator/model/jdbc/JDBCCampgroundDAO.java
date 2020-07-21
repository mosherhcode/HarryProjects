package com.techelevator.model.jdbc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Campground;
import com.techelevator.model.CampgroundDAO;
import com.techelevator.model.Request;

public class JDBCCampgroundDAO implements CampgroundDAO {

	private JdbcTemplate jdbcTemplate;
	
	public JDBCCampgroundDAO(DataSource datasource) {
		jdbcTemplate = new JdbcTemplate(datasource);
	}

	@Override
	public List<Campground> viewAllCampgroundsInPark(Request request) {
		List<Campground> campgrounds = new ArrayList<Campground>();
		
		String sqlViewAllCampgroundsInPark = 
				"SELECT * "
				+ "FROM campground c "
				+ "INNER JOIN park p ON c.park_id = p.park_id "
				+ "WHERE p.park_id = ? "
				+ "ORDER BY c.name"; 
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlViewAllCampgroundsInPark, request.getDesiredPark().getParkID());
				
		while(results.next()) {
			Campground theCampground = mapRowToCampground(results.getLong("campground_id"),  
					results.getString("name"), results.getString("open_from_mm"), results.getString("open_to_mm"), 
					results.getBigDecimal("daily_fee"));
			campgrounds.add(theCampground);
		}
		return campgrounds;
	}
	
	@Override
	public Campground getCampgroundFromId(long campgroundId) {
		String sqlGetCampgroundById = "SELECT * FROM campground WHERE campground_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetCampgroundById, campgroundId); 
		results.next();
		Campground theCampground = mapRowToCampground(results.getLong("campground_id"), results.getString("name"), 
				results.getString("open_from_mm"), results.getString("open_to_mm"), results.getBigDecimal("daily_fee"));
		return theCampground;
	}
	
	private Campground mapRowToCampground(long campgroundId, String name, String openFromMonth, String openToMonth,
			BigDecimal dailyFee) {
		Campground theCampground = new Campground();
		theCampground.setCampgroundId(campgroundId);
		theCampground.setName(name);
		theCampground.setOpenDate(LocalDate.of(0, Integer.parseInt(openFromMonth), 1));
		theCampground.setClosingDate(LocalDate.of(0, Integer.parseInt(openToMonth), 1));
		theCampground.setDailyFee(dailyFee);
		
		
		return theCampground;
	}

	

}
