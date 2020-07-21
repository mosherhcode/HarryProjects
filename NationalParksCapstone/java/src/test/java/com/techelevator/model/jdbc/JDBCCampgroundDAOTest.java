package com.techelevator.model.jdbc;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.DAOIntegrationTest;
import com.techelevator.model.Campground;
import com.techelevator.model.CampgroundDAO;
import com.techelevator.model.Park;
import com.techelevator.model.Request;

public class JDBCCampgroundDAOTest extends DAOIntegrationTest {

	private JdbcTemplate jdbcTemplate;
	private CampgroundDAO dao;	

	@Test
	public void testViewAllCampgroundsInPark() {
		dao = new JDBCCampgroundDAO(getDataSource());
		jdbcTemplate = new JdbcTemplate(getDataSource());
		
		//Get a Park ID to Fill a Request
		String sqlGetParkId = "SELECT * FROM park LIMIT 1";
		SqlRowSet parkId = jdbcTemplate.queryForRowSet(sqlGetParkId);
		parkId.next();
		Park park = new Park();
		park.setParkID(parkId.getLong("park_id"));
		
		//Fill Request with Park ID
		Request request = new Request();
		request.setDesiredPark(park);
		
		//Use Prepared Request to Get expectedCount
		String sqlCountAllCampgroundsInPark = "SELECT COUNT(*) FROM park p JOIN campground c ON p.park_id = "
				+ "c.park_id WHERE p.park_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlCountAllCampgroundsInPark, park.getParkID());
		results.next();
		int expectedCount = results.getInt(1);
		
		// Run Actual Method with Same Request to Get resultCount 
		int resultCount = dao.viewAllCampgroundsInPark(request).size();
		
		assertEquals(expectedCount, resultCount);
	}
	
	@Test
	public void test_getCampgroundFromId() {
		jdbcTemplate = new JdbcTemplate(getDataSource());
		dao = new JDBCCampgroundDAO(getDataSource());
		
		long testCampgroundId = jdbcTemplate.queryForObject("SELECT campground_id FROM campground LIMIT 1", Long.class);
		
		String getExpectedCampgroundFromId = "SELECT * FROM campground WHERE campground_id = ?";
		SqlRowSet expectedResults = jdbcTemplate.queryForRowSet(getExpectedCampgroundFromId, testCampgroundId);
		expectedResults.next();
		Campground expectedCampground = mapRowToCampground(expectedResults.getLong("campground_id"), expectedResults.getString("name"), 
				expectedResults.getString("open_from_mm"), expectedResults.getString("open_to_mm"), expectedResults.getBigDecimal("daily_fee"));
		
		Campground actualCampground = dao.getCampgroundFromId(testCampgroundId);
		assertCampgroundsEqual(expectedCampground, actualCampground);
	}
	
	private void assertCampgroundsEqual(Campground expectedCampground, Campground actualCampground) {
		assertEquals(expectedCampground.getCampgroundId(), actualCampground.getCampgroundId());
		assertEquals(expectedCampground.getName(), actualCampground.getName());
		assertEquals(expectedCampground.getOpenDate(), actualCampground.getOpenDate());
		assertEquals(expectedCampground.getClosingDate(), actualCampground.getClosingDate());
		assertEquals(expectedCampground.getDailyFee(), actualCampground.getDailyFee());
		
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
