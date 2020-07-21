package com.techelevator.model.jdbc;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Park;
import com.techelevator.model.Request;
import com.techelevator.model.Reservation;
import com.techelevator.model.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO {

	JdbcTemplate jdbcTemplate;
	
	public JDBCReservationDAO(DataSource datasource) {
		jdbcTemplate = new JdbcTemplate(datasource);
	}

	@Override
	public Reservation addReservation(Request request) {
		
		String sqlInsertReservation = 	"INSERT INTO reservation (site_id, name, from_date, to_date) "
										+ "VALUES (?,?,?,?) RETURNING reservation_id";
		
		long reservationId = jdbcTemplate.queryForObject(sqlInsertReservation, Long.class, 
				request.getDesiredCampsite().getCampsiteId(), request.getPartyName(), Date.valueOf(request.getFromDate()), 
						Date.valueOf(request.getToDate()));
		
		Reservation theReservation = mapRowToReservation(reservationId,
				request.getDesiredCampsite().getCampsiteId(), request.getPartyName(),request.getFromDate(),request.getToDate(), LocalDate.now());
		
		return theReservation;
	}



	@Override
	public boolean cancelReservation(Long reservationId) {
		String sqlRemoveReservation = 	"DELETE FROM reservation "
										+ "WHERE reservation_id = ? ";
		try {
			jdbcTemplate.update(sqlRemoveReservation, reservationId);
		} catch(DataAccessException dae) {
			return false;
		}
		return true;
	}

	@Override
	public List<Reservation> getAllReservationsInParkforNext30Days(Park park) {
		List<Reservation> reservations = new ArrayList<Reservation>();
		String sqlgetAllReservationsInParkNext30Days = 
												"SELECT r.* "
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
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlgetAllReservationsInParkNext30Days, park.getParkID(), 
				Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now().plus(30, ChronoUnit.DAYS)));
		
		while(results.next()) {
			Reservation theReservation = mapRowToReservation(results.getLong("reservation_id"), results.getLong("site_id"), 
					results.getString("name"), results.getDate("from_date").toLocalDate(), results.getDate("to_date").toLocalDate(), 
					results.getDate("create_date").toLocalDate());
			reservations.add(theReservation);
		}
		
		
		return reservations;
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
