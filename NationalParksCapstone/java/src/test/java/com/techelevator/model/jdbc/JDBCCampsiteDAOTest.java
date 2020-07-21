package com.techelevator.model.jdbc;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.DAOIntegrationTest;
import com.techelevator.model.Campground;
import com.techelevator.model.CampsiteDAO;
import com.techelevator.model.Park;
import com.techelevator.model.Request;

public class JDBCCampsiteDAOTest extends DAOIntegrationTest {

	private JdbcTemplate jdbcTemplate;
	private CampsiteDAO dao;
	private LocalDate nearFromDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue() + 1, 5);
	private LocalDate nearToDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue() + 1, 12);
	private LocalDate futureFromDate = LocalDate.of(LocalDate.now().getYear() + 2, LocalDate.now().getMonthValue() + 1, 5);
	private LocalDate futureToDate = LocalDate.of(LocalDate.now().getYear() + 2, LocalDate.now().getMonthValue() + 1, 12);
	
	@Test
	public void testViewAllCampsitesFromFULLRequirementsRequestFutureDates() {
		dao = new JDBCCampsiteDAO(getDataSource());
		jdbcTemplate = new JdbcTemplate(getDataSource());
		Request request = new Request();
		request.setTesting(true);  //SET isTesting to TRUE IN ORDER TO REMOVE LIMIT SQL STATEMENT WHEN RUNNING
		
		//CREATE OBJECTS
		Park park = new Park();
		Campground campground = new Campground();
		//RUN QUERY FOR 1 CAMPGROUND AND FILL OBJECTS WITH IDS
		String sqlGetID = "SELECT * FROM campground LIMIT 1";
		SqlRowSet get1Result = jdbcTemplate.queryForRowSet(sqlGetID);
		get1Result.next();
		park.setParkID(get1Result.getLong("park_id"));
		campground.setCampgroundId(get1Result.getLong("campground_id"));
		//FILL REQUESTS WITH PARK/CAMPGROUND IDS
		request.setDesiredPark(park);
		request.setDesiredCampground(campground);

		//FILL REQUEST WITH REMAINING ITEMS AS TESTING REQUIRES
		request.setFromDate(futureFromDate);
		request.setToDate(futureToDate);
		request.setNeedsUtilities(true);
		request.setNeedsAccessible(true);
		request.setRvLength(20);
		request.setPartySize(4);
		
		// Use Prepared Request to Get expectedCount
		String sqlGetAllCampsitesFromRequest = "SELECT s.*, c.name, c.daily_fee "
				+ "FROM site s "
				+ "INNER JOIN campground c ON s.campground_id = c.campground_id "
				+ "WHERE s.campground_id = ? "  				//ITEM 1 request.getDesiredCampground().getCampgroundId()
				+ "AND s.max_occupancy >= ? "					//ITEM 2 request.getPartySize()
				+ "AND s.max_rv_length >= ? "					//ITEM 3 request.getRvLength()
				+ "AND s.utilities IS TRUE "						//COMMENT OUT AS NECESSARY
				+ "AND s.accessible IS TRUE "						//COMMENT OUT AS NECESSARY
				+ "AND ((c.open_from_mm::integer <= ?) "		//ITEM 4 request.getFromDate().getMonthValue()
				+ "AND (c.open_to_mm::integer >= ?)) "			//ITEM 5 request.getToDate().getMonthValue()
				+ "AND s.site_id "
				+ "NOT IN (SELECT site_id "
				+ "FROM reservation "
				+ "WHERE (from_date <= ? AND to_date >= ?) "	//ITEM 6 AND 7 (BOTH fromDate)  request.getFromDate(), request.getFromDate(),
				+ "OR "
				+ "(from_date <= ? AND to_date >= ?)) "			//ITEM 8 AND 9 (BOTH toDate)	request.getToDate(), request.getToDate()
				+ "GROUP BY s.campground_id, s.site_id, c.name, c.daily_fee "
				+ "ORDER BY c.daily_fee ASC ";
				//NO LIMIT SINCE FEEDING isTesting AS PART OF REQUEST ITEM.
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllCampsitesFromRequest,
				request.getDesiredCampground().getCampgroundId(),
				request.getPartySize(),
				request.getRvLength(),
				request.getFromDate().getMonthValue(),
				request.getToDate().getMonthValue(),
				request.getFromDate(),
				request.getFromDate(),
				request.getToDate(),
				request.getToDate());
		
		int expectedCount = 0;

		while (results.next()) {
			expectedCount += 1;
		}

		// Run Actual Method with Same Request to Get resultCount
		int resultCount = dao.viewAvailableCampsitesFromRequest(request).size();

		assertEquals(expectedCount, resultCount);
	}
	
	@Test
	public void testViewAllCampsitesFromFullRequirementsRequestWithNullCampgroundIDFutureDates() {
		dao = new JDBCCampsiteDAO(getDataSource());
		jdbcTemplate = new JdbcTemplate(getDataSource());
		Request request = new Request();
		request.setTesting(true);  //SET isTesting to TRUE IN ORDER TO REMOVE LIMIT SQL STATEMENT WHEN RUNNING
		
		//CREATE OBJECTS
		Park park = new Park();
		Campground campground = new Campground();
		//RUN QUERY FOR 1 CAMPGROUND AND FILL OBJECTS WITH IDS
		String sqlGetID = "SELECT * FROM campground LIMIT 1";
		SqlRowSet get1Result = jdbcTemplate.queryForRowSet(sqlGetID);
		get1Result.next();
		park.setParkID(get1Result.getLong("park_id"));
		campground.setCampgroundId(get1Result.getLong("campground_id"));
		//FILL REQUESTS WITH PARK/CAMPGROUND IDS
		request.setDesiredPark(park);
		request.setDesiredCampground(campground);

		//FILL REQUEST WITH REMAINING ITEMS AS TESTING REQUIRES
		request.setFromDate(futureFromDate);
		request.setToDate(futureToDate);
		request.setNeedsUtilities(true);
		request.setNeedsAccessible(true);
		request.setRvLength(20);
		request.setPartySize(4);
		
		// Use Prepared Request to Get expectedCount
		String sqlGetAllCampsitesFromRequest = "SELECT s.*, c.name, c.daily_fee "
				+ "FROM site s "
				+ "INNER JOIN campground c ON s.campground_id = c.campground_id "
				+ "WHERE s.campground_id = ? "  				//ITEM 1 request.getDesiredCampground().getCampgroundId()
				+ "AND s.max_occupancy >= ? "					//ITEM 2 request.getPartySize()
				+ "AND s.max_rv_length >= ? "					//ITEM 3 request.getRvLength()
				+ "AND s.utilities IS TRUE "						//COMMENT OUT AS NECESSARY
				+ "AND s.accessible IS TRUE "						//COMMENT OUT AS NECESSARY
				+ "AND ((c.open_from_mm::integer <= ?) "		//ITEM 4 request.getFromDate().getMonthValue()
				+ "AND (c.open_to_mm::integer >= ?)) "			//ITEM 5 request.getToDate().getMonthValue()
				+ "AND s.site_id "
				+ "NOT IN (SELECT site_id "
				+ "FROM reservation "
				+ "WHERE (from_date <= ? AND to_date >= ?) "	//ITEM 6 AND 7 (BOTH fromDate)  request.getFromDate(), request.getFromDate(),
				+ "OR "
				+ "(from_date <= ? AND to_date >= ?)) "			//ITEM 8 AND 9 (BOTH toDate)	request.getToDate(), request.getToDate()
				+ "GROUP BY s.campground_id, s.site_id, c.name, c.daily_fee "
				+ "ORDER BY c.daily_fee ASC ";
				//NO LIMIT SINCE FEEDING isTesting AS PART OF REQUEST ITEM.
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllCampsitesFromRequest,
				request.getDesiredCampground().getCampgroundId(),
				request.getPartySize(),
				request.getRvLength(),
				request.getFromDate().getMonthValue(),
				request.getToDate().getMonthValue(),
				request.getFromDate(),
				request.getFromDate(),
				request.getToDate(),
				request.getToDate());
		
		int expectedCount = 0;

		while (results.next()) {
			expectedCount += 1;
		}

		//Prep request with null campground even though I needed it above.
		request.setDesiredCampground(null);
				
		// Run Actual Method with Same Request to Get resultCount
		int resultCount = dao.viewAvailableCampsitesFromRequest(request).size();

		assertEquals(expectedCount, resultCount);
	}
	
	@Test
	public void testViewAllCampsitesFromEmptyRequirementsRequestFutureDates() {
		dao = new JDBCCampsiteDAO(getDataSource());
		jdbcTemplate = new JdbcTemplate(getDataSource());
		Request request = new Request();
		request.setTesting(true);  //SET isTesting to TRUE IN ORDER TO REMOVE LIMIT SQL STATEMENT WHEN RUNNING
		
		//CREATE OBJECTS
		Park park = new Park();
		Campground campground = new Campground();
		//RUN QUERY FOR 1 CAMPGROUND AND FILL OBJECTS WITH IDS
		String sqlGetID = "SELECT * FROM campground LIMIT 1";
		SqlRowSet get1Result = jdbcTemplate.queryForRowSet(sqlGetID);
		get1Result.next();
		park.setParkID(get1Result.getLong("park_id"));
		campground.setCampgroundId(get1Result.getLong("campground_id"));
		//FILL REQUESTS WITH PARK/CAMPGROUND IDS
		request.setDesiredPark(park);
		request.setDesiredCampground(campground);

		//FILL REQUEST WITH REMAINING ITEMS AS TESTING REQUIRES
		request.setFromDate(futureFromDate);
		request.setToDate(futureToDate);
		request.setNeedsUtilities(false);
		request.setNeedsAccessible(false);
		//NO RV IN THIS TEST
		request.setPartySize(1);
		
		// Use Prepared Request to Get expectedCount
		String sqlGetAllCampsitesFromRequest = "SELECT s.*, c.name, c.daily_fee "
				+ "FROM site s "
				+ "INNER JOIN campground c ON s.campground_id = c.campground_id "
				+ "WHERE s.campground_id = ? "  				//ITEM 1 request.getDesiredCampground().getCampgroundId()
				+ "AND s.max_occupancy >= ? "					//ITEM 2 request.getPartySize()
				//NO RV REQUIREMENTS
				//NO UTILITIES REQUIREMENTS
				//NO ACCESSIBILITY REQUIREMENTS
				+ "AND ((c.open_from_mm::integer <= ?) "		//ITEM 4 request.getFromDate().getMonthValue()
				+ "AND (c.open_to_mm::integer >= ?)) "			//ITEM 5 request.getToDate().getMonthValue()
				+ "AND s.site_id "
				+ "NOT IN (SELECT site_id "
				+ "FROM reservation "
				+ "WHERE (from_date <= ? AND to_date >= ?) "	//ITEM 6 AND 7 (BOTH fromDate)  request.getFromDate(), request.getFromDate(),
				+ "OR "
				+ "(from_date <= ? AND to_date >= ?)) "			//ITEM 8 AND 9 (BOTH toDate)	request.getToDate(), request.getToDate()
				+ "GROUP BY s.campground_id, s.site_id, c.name, c.daily_fee "
				+ "ORDER BY c.daily_fee ASC ";
				//NO LIMIT SINCE FEEDING isTesting AS PART OF REQUEST ITEM.
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllCampsitesFromRequest,
				request.getDesiredCampground().getCampgroundId(),
				request.getPartySize(),
				request.getFromDate().getMonthValue(),
				request.getToDate().getMonthValue(),
				request.getFromDate(),
				request.getFromDate(),
				request.getToDate(),
				request.getToDate());
		
		int expectedCount = 0;

		while (results.next()) {
			expectedCount += 1;
		}

		// Run Actual Method with Same Request to Get resultCount
		int resultCount = dao.viewAvailableCampsitesFromRequest(request).size();

		assertEquals(expectedCount, resultCount);
	}
	
	//NEARING DATE TESTS
	
	@Test
	public void testViewAllCampsitesFromFULLRequirementsRequestNearingDates() {
		dao = new JDBCCampsiteDAO(getDataSource());
		jdbcTemplate = new JdbcTemplate(getDataSource());
		Request request = new Request();
		request.setTesting(true);  //SET isTesting to TRUE IN ORDER TO REMOVE LIMIT SQL STATEMENT WHEN RUNNING
		
		//CREATE OBJECTS
		Park park = new Park();
		Campground campground = new Campground();
		//RUN QUERY FOR 1 CAMPGROUND AND FILL OBJECTS WITH IDS
		String sqlGetID = "SELECT * FROM campground LIMIT 1";
		SqlRowSet get1Result = jdbcTemplate.queryForRowSet(sqlGetID);
		get1Result.next();
		park.setParkID(get1Result.getLong("park_id"));
		campground.setCampgroundId(get1Result.getLong("campground_id"));
		//FILL REQUESTS WITH PARK/CAMPGROUND IDS
		request.setDesiredPark(park);
		request.setDesiredCampground(campground);

		//FILL REQUEST WITH REMAINING ITEMS AS TESTING REQUIRES
		request.setFromDate(nearFromDate);
		request.setToDate(nearToDate);
		request.setNeedsUtilities(true);
		request.setNeedsAccessible(true);
		request.setRvLength(20);
		request.setPartySize(4);
		
		// Use Prepared Request to Get expectedCount
		String sqlGetAllCampsitesFromRequest = "SELECT s.*, c.name, c.daily_fee "
				+ "FROM site s "
				+ "INNER JOIN campground c ON s.campground_id = c.campground_id "
				+ "WHERE s.campground_id = ? "  				//ITEM 1 request.getDesiredCampground().getCampgroundId()
				+ "AND s.max_occupancy >= ? "					//ITEM 2 request.getPartySize()
				+ "AND s.max_rv_length >= ? "					//ITEM 3 request.getRvLength()
				+ "AND s.utilities IS TRUE "						//COMMENT OUT AS NECESSARY
				+ "AND s.accessible IS TRUE "						//COMMENT OUT AS NECESSARY
				+ "AND ((c.open_from_mm::integer <= ?) "		//ITEM 4 request.getFromDate().getMonthValue()
				+ "AND (c.open_to_mm::integer >= ?)) "			//ITEM 5 request.getToDate().getMonthValue()
				+ "AND s.site_id "
				+ "NOT IN (SELECT site_id "
				+ "FROM reservation "
				+ "WHERE (from_date <= ? AND to_date >= ?) "	//ITEM 6 AND 7 (BOTH fromDate)  request.getFromDate(), request.getFromDate(),
				+ "OR "
				+ "(from_date <= ? AND to_date >= ?)) "			//ITEM 8 AND 9 (BOTH toDate)	request.getToDate(), request.getToDate()
				+ "GROUP BY s.campground_id, s.site_id, c.name, c.daily_fee "
				+ "ORDER BY c.daily_fee ASC ";
				//NO LIMIT SINCE FEEDING isTesting AS PART OF REQUEST ITEM.
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllCampsitesFromRequest,
				request.getDesiredCampground().getCampgroundId(),
				request.getPartySize(),
				request.getRvLength(),
				request.getFromDate().getMonthValue(),
				request.getToDate().getMonthValue(),
				request.getFromDate(),
				request.getFromDate(),
				request.getToDate(),
				request.getToDate());
		
		int expectedCount = 0;

		while (results.next()) {
			expectedCount += 1;
		}

		// Run Actual Method with Same Request to Get resultCount
		int resultCount = dao.viewAvailableCampsitesFromRequest(request).size();

		assertEquals(expectedCount, resultCount);
	}
	
	@Test
	public void testViewAllCampsitesFromFullRequirementsRequestWithNullCampgroundIDNearingDates() {
		dao = new JDBCCampsiteDAO(getDataSource());
		jdbcTemplate = new JdbcTemplate(getDataSource());
		Request request = new Request();
		request.setTesting(true);  //SET isTesting to TRUE IN ORDER TO REMOVE LIMIT SQL STATEMENT WHEN RUNNING
		
		//CREATE OBJECTS
		Park park = new Park();
		Campground campground = new Campground();
		//RUN QUERY FOR 1 CAMPGROUND AND FILL OBJECTS WITH IDS
		String sqlGetID = "SELECT * FROM campground LIMIT 1";
		SqlRowSet get1Result = jdbcTemplate.queryForRowSet(sqlGetID);
		get1Result.next();
		park.setParkID(get1Result.getLong("park_id"));
		campground.setCampgroundId(get1Result.getLong("campground_id"));
		//FILL REQUESTS WITH PARK/CAMPGROUND IDS
		request.setDesiredPark(park);
		request.setDesiredCampground(campground);

		//FILL REQUEST WITH REMAINING ITEMS AS TESTING REQUIRES
		request.setFromDate(nearFromDate);
		request.setToDate(nearToDate);
		request.setNeedsUtilities(true);
		request.setNeedsAccessible(true);
		request.setRvLength(20);
		request.setPartySize(4);
		
		// Use Prepared Request to Get expectedCount
		String sqlGetAllCampsitesFromRequest = "SELECT s.*, c.name, c.daily_fee "
				+ "FROM site s "
				+ "INNER JOIN campground c ON s.campground_id = c.campground_id "
				+ "WHERE s.campground_id = ? "  				//ITEM 1 request.getDesiredCampground().getCampgroundId()
				+ "AND s.max_occupancy >= ? "					//ITEM 2 request.getPartySize()
				+ "AND s.max_rv_length >= ? "					//ITEM 3 request.getRvLength()
				+ "AND s.utilities IS TRUE "						//COMMENT OUT AS NECESSARY
				+ "AND s.accessible IS TRUE "						//COMMENT OUT AS NECESSARY
				+ "AND ((c.open_from_mm::integer <= ?) "		//ITEM 4 request.getFromDate().getMonthValue()
				+ "AND (c.open_to_mm::integer >= ?)) "			//ITEM 5 request.getToDate().getMonthValue()
				+ "AND s.site_id "
				+ "NOT IN (SELECT site_id "
				+ "FROM reservation "
				+ "WHERE (from_date <= ? AND to_date >= ?) "	//ITEM 6 AND 7 (BOTH fromDate)  request.getFromDate(), request.getFromDate(),
				+ "OR "
				+ "(from_date <= ? AND to_date >= ?)) "			//ITEM 8 AND 9 (BOTH toDate)	request.getToDate(), request.getToDate()
				+ "GROUP BY s.campground_id, s.site_id, c.name, c.daily_fee "
				+ "ORDER BY c.daily_fee ASC ";
				//NO LIMIT SINCE FEEDING isTesting AS PART OF REQUEST ITEM.
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllCampsitesFromRequest,
				request.getDesiredCampground().getCampgroundId(),
				request.getPartySize(),
				request.getRvLength(),
				request.getFromDate().getMonthValue(),
				request.getToDate().getMonthValue(),
				request.getFromDate(),
				request.getFromDate(),
				request.getToDate(),
				request.getToDate());
		
		int expectedCount = 0;

		while (results.next()) {
			expectedCount += 1;
		}

		//Prep request with null campground eventhough i needed it above.
		request.setDesiredCampground(null);
				
		// Run Actual Method with Same Request to Get resultCount
		int resultCount = dao.viewAvailableCampsitesFromRequest(request).size();

		assertEquals(expectedCount, resultCount);
	}
	
	@Test
	public void testViewAllCampsitesFromEmptyRequirementsRequestNearingDates() {
		dao = new JDBCCampsiteDAO(getDataSource());
		jdbcTemplate = new JdbcTemplate(getDataSource());
		Request request = new Request();
		request.setTesting(true);  //SET isTesting to TRUE IN ORDER TO REMOVE LIMIT SQL STATEMENT WHEN RUNNING
		
		//CREATE OBJECTS
		Park park = new Park();
		Campground campground = new Campground();
		//RUN QUERY FOR 1 CAMPGROUND AND FILL OBJECTS WITH IDS
		String sqlGetID = "SELECT * FROM campground LIMIT 1";
		SqlRowSet get1Result = jdbcTemplate.queryForRowSet(sqlGetID);
		get1Result.next();
		park.setParkID(get1Result.getLong("park_id"));
		campground.setCampgroundId(get1Result.getLong("campground_id"));
		//FILL REQUESTS WITH PARK/CAMPGROUND IDS
		request.setDesiredPark(park);
		request.setDesiredCampground(campground);

		//FILL REQUEST WITH REMAINING ITEMS AS TESTING REQUIRES
		request.setFromDate(nearFromDate);
		request.setToDate(nearToDate);
		request.setNeedsUtilities(false);
		request.setNeedsAccessible(false);
		//NO RV IN THIS TEST
		request.setPartySize(1);
		
		// Use Prepared Request to Get expectedCount
		String sqlGetAllCampsitesFromRequest = "SELECT s.*, c.name, c.daily_fee "
				+ "FROM site s "
				+ "INNER JOIN campground c ON s.campground_id = c.campground_id "
				+ "WHERE s.campground_id = ? "  				//ITEM 1 request.getDesiredCampground().getCampgroundId()
				+ "AND s.max_occupancy >= ? "					//ITEM 2 request.getPartySize()
				//NO RV REQUIREMENTS
				//NO UTILITIES REQUIREMENTS
				//NO ACCESSIBILITY REQUIREMENTS
				+ "AND ((c.open_from_mm::integer <= ?) "		//ITEM 4 request.getFromDate().getMonthValue()
				+ "AND (c.open_to_mm::integer >= ?)) "			//ITEM 5 request.getToDate().getMonthValue()
				+ "AND s.site_id "
				+ "NOT IN (SELECT site_id "
				+ "FROM reservation "
				+ "WHERE (from_date <= ? AND to_date >= ?) "	//ITEM 6 AND 7 (BOTH fromDate)  request.getFromDate(), request.getFromDate(),
				+ "OR "
				+ "(from_date <= ? AND to_date >= ?)) "			//ITEM 8 AND 9 (BOTH toDate)	request.getToDate(), request.getToDate()
				+ "GROUP BY s.campground_id, s.site_id, c.name, c.daily_fee "
				+ "ORDER BY c.daily_fee ASC ";
				//NO LIMIT SINCE FEEDING isTesting AS PART OF REQUEST ITEM.
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllCampsitesFromRequest,
				request.getDesiredCampground().getCampgroundId(),
				request.getPartySize(),
				request.getFromDate().getMonthValue(),
				request.getToDate().getMonthValue(),
				request.getFromDate(),
				request.getFromDate(),
				request.getToDate(),
				request.getToDate());
		
		int expectedCount = 0;

		while (results.next()) {
			expectedCount += 1;
		}

		// Run Actual Method with Same Request to Get resultCount
		int resultCount = dao.viewAvailableCampsitesFromRequest(request).size();

		assertEquals(expectedCount, resultCount);
	}

}
