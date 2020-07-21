package com.techelevator.model.jdbc;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.DAOIntegrationTest;
import com.techelevator.model.ParkDAO;

public class JDBCParkDAOTest extends DAOIntegrationTest {

	private JdbcTemplate jdbcTemplate;
	private ParkDAO dao;	

	@Test
	public void testViewAllParks() {
		dao = new JDBCParkDAO(super.getDataSource());
		jdbcTemplate = new JdbcTemplate(super.getDataSource());
		String sqlCountAllParks = "SELECT COUNT(*) FROM park";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlCountAllParks);
		results.next();
		
		int expectedCount = results.getInt(1);
		int resultCount = dao.viewAllParks().size();
		
		assertEquals(expectedCount, resultCount);
	}

}
