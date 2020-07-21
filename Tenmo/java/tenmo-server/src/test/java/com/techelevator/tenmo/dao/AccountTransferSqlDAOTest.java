package com.techelevator.tenmo.dao;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.tenmo.model.Transfer;


public class AccountTransferSqlDAOTest {

	private static final double TRANSFER_1_AMOUNT = 100.00;
	private static final double TRANSFER_2_AMOUNT = 200.00;
	private static SingleConnectionDataSource dataSource;
	private AccountTransferSqlDAO dao;
	private UserSqlDAO userDao;
	private JdbcTemplate jdbcTemplate;
	private long transfer1Id;
	private long transfer2Id;
	
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}
		
	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}
	
	@Before
	public void setup() {
		jdbcTemplate = new JdbcTemplate(dataSource);
		dao = new AccountTransferSqlDAO(jdbcTemplate);
		userDao = new UserSqlDAO(jdbcTemplate);
		userDao.create("Margaret", "password");
		userDao.create("Terrance", "password");
		Transfer transfer1 = new Transfer();
		transfer1.setTransferType("Send");
		transfer1.setAccountFrom(jdbcTemplate.queryForObject("SELECT user_id FROM users WHERE username = 'Margaret'", Long.class));
		transfer1.setAccountTo(jdbcTemplate.queryForObject("SELECT user_id FROM users WHERE username = 'Terrance'", Long.class));
		transfer1.setAmount(BigDecimal.valueOf(TRANSFER_1_AMOUNT));		
		transfer1Id = dao.makeTransfer(transfer1).getTransferId();
		Transfer transfer2 = new Transfer();
		transfer2.setTransferType("Send");
		transfer2.setAccountFrom(jdbcTemplate.queryForObject("SELECT user_id FROM users WHERE username = 'Margaret'", Long.class));
		transfer2.setAccountTo(jdbcTemplate.queryForObject("SELECT user_id FROM users WHERE username = 'Terrance'", Long.class));
		transfer2.setAmount(BigDecimal.valueOf(TRANSFER_2_AMOUNT));		
		transfer2Id = dao.makeTransfer(transfer2).getTransferId();
	}
	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void view_account_balance_for_last_user_returns_correct_amount() {
		String sqlGetUserId = "SELECT MAX(user_id) FROM accounts";
		long id = jdbcTemplate.queryForObject(sqlGetUserId, Long.class);
		
		String sqlGetBalance = "SELECT balance FROM accounts WHERE user_id = ?";
		BigDecimal expectedBalance = jdbcTemplate.queryForObject(sqlGetBalance, BigDecimal.class, id);
		
		BigDecimal actualBalance = dao.viewCurrentBalance(id).setScale(2);
		
		assertEquals(expectedBalance, actualBalance);
	}
	
	@Test
	public void view_transfer_history_returns_correct_list_of_transfers() {
		
		long id = jdbcTemplate.queryForObject("SELECT user_id FROM users WHERE username = 'Margaret'", Long.class);
		
		String sqlGetCountOfTransfers = "SELECT t.transfer_id " + 
				"FROM transfers t " +
					"INNER JOIN accounts acc1 ON t.account_from = acc1.account_id " +  
					"INNER JOIN accounts acc2 ON t.account_to = acc2.account_id " +
				"WHERE " + 
				"acc1.user_id = ? " + 
				"OR " + 
				"acc2.user_id = ? " +
				"GROUP BY t.transfer_id ";
				
		
		SqlRowSet expectedResults = jdbcTemplate.queryForRowSet(sqlGetCountOfTransfers, id, id);
		int expectedCount = 0;
		while(expectedResults.next())
			expectedCount ++;
		
		int actualCount = dao.viewTransferHistory(id).length;
		
		assertEquals(expectedCount, actualCount);
	}

	
	@Test
	public void view_transfer_details_test() {
		Transfer actualTransfer = dao.viewTransferDetails(transfer1Id);
		
		assertEquals(actualTransfer.getAmount().setScale(2), BigDecimal.valueOf(TRANSFER_1_AMOUNT).setScale(2));
		assertEquals(actualTransfer.getTransferId(), transfer1Id);
		
	}
	
	@Test
	public void make_transfer_test() {
		
		long id = jdbcTemplate.queryForObject("SELECT user_id FROM users WHERE username = 'Margaret'", Long.class);
		
		BigDecimal balanceBefore = dao.viewCurrentBalance(id);
		
//		dao.makeTransfer(transfer);
	}
	
}
