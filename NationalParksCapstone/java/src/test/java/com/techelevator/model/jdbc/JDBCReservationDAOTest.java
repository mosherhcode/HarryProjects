package com.techelevator.model.jdbc;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.DAOIntegrationTest;
import com.techelevator.model.Campsite;
import com.techelevator.model.Park;
import com.techelevator.model.Request;
import com.techelevator.model.Reservation;
import com.techelevator.model.ReservationDAO;

public class JDBCReservationDAOTest extends DAOIntegrationTest {

	private JdbcTemplate jdbcTemplate;
	private ReservationDAO dao;	
	
	@Test
	public void test_addReservation(){
		jdbcTemplate = new JdbcTemplate(super.getDataSource());
		Request request = new Request();
		request.setFromDate(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue() + 1, 5));
		request.setToDate(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue() + 1, 12));
		request.setPartyName("Test Party");
		Campsite testCampsite = new Campsite();
		testCampsite.setCampsiteId(jdbcTemplate.queryForObject("SELECT site_id FROM site LIMIT 1", Long.class));
		request.setDesiredCampsite(testCampsite);
		
		dao = new JDBCReservationDAO(super.getDataSource());
		Reservation actualReservation = dao.addReservation(request);
		
		
		String sqlFindNewReservation = "SELECT * FROM reservation WHERE reservation_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindNewReservation, actualReservation.getReservationId());
		
		results.next();
		
		long testReservationId = results.getLong("reservation_id");
		
		assertEquals(testReservationId, actualReservation.getReservationId());
		
	}
	
	@Test
	public void test_getAllReservationsInParkforNext30Days() {
		jdbcTemplate = new JdbcTemplate(super.getDataSource());
		dao = new JDBCReservationDAO(super.getDataSource());
		String sqlGetTestPark = "SELECT park_id FROM park LIMIT 1";
		long testParkId = jdbcTemplate.queryForObject(sqlGetTestPark, Long.class);
		Park testPark = new Park();
		testPark.setParkID(testParkId);
		
		String sqlGetAllReservationsInParkForNext30Days = "SELECT r.* "
															+ "FROM "
															+ "reservation r "
															+ "INNER JOIN site s "
																+ "ON r.site_id = s.site_id "
																+ "INNER JOIN campground c "
																	+ "ON s.campground_id = c.campground_id "
															+ "WHERE c.park_id = ? "
															+ "AND r.from_date >= ? "
															+ "AND r.from_date <= ? "
															+ "ORDER BY r.from_date";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllReservationsInParkForNext30Days, testPark.getParkID(), 
		Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now().plus(30, ChronoUnit.DAYS)));
		
		List<Reservation> expectedReservations = new ArrayList<Reservation>(0);
		
		while(results.next()) {
			Reservation theReservation = mapRowToReservation(results.getLong("reservation_id"), results.getLong("site_id"), 
					results.getString("name"), results.getDate("from_date").toLocalDate(), results.getDate("to_date").toLocalDate(), 
					results.getDate("create_date").toLocalDate());
			expectedReservations.add(theReservation);
		}
		
		List<Reservation> actualReservations = dao.getAllReservationsInParkforNext30Days(testPark);
		
		assertEquals(expectedReservations.size(), actualReservations.size());
		
		
		
	}

	private Reservation mapRowToReservation(long reservationId, long campsiteId, String partyName, LocalDate fromDate,
			LocalDate toDate, LocalDate createdDate) {
		
		Reservation theReservation = new Reservation();
		theReservation.setReservationId(reservationId);
		theReservation.setSiteId(campsiteId);
		theReservation.setName(partyName);
		theReservation.setFromDate(fromDate);
		theReservation.setToDate(toDate);
		theReservation.setCreatedDate(createdDate);
		return theReservation;
	}
	
	
}
