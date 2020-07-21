package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Transfer;

@Component
public class AccountTransferSqlDAO implements AccountTransferDAO {

	JdbcTemplate jdbcTemplate;
	
	public AccountTransferSqlDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public BigDecimal viewCurrentBalance(long id) {
		String sql = "SELECT balance FROM accounts WHERE user_id = ?";
		BigDecimal balance = BigDecimal.valueOf(jdbcTemplate.queryForObject(sql, Double.class, id));
		return balance;
	}
	
	@Override
	public Transfer[] viewPendingRequests(long id) {
		//TRYING TO GET BELOW CODE IN WORKING ORDER

		List<Transfer> transfers = new ArrayList<>(0);		
		String sql = "SELECT " + 
						"t.transfer_id, " + 
						"t.amount, " + 
						"t.account_from, " +
						"t.account_to, " +
						"ts.transfer_status_desc, " +
						"tt.transfer_type_desc, " +
						"u.username " +
					"FROM " +
						"transfers t " +
							"INNER JOIN accounts a " +
								"ON t.account_to = a.account_id " +
									"INNER JOIN users u " +
									 	"ON a.user_id = u.user_id " +
							"INNER JOIN transfer_types tt " +
								"ON t.transfer_type_id = tt.transfer_type_id " +
							"INNER JOIN transfer_statuses ts " +
								"ON t.transfer_status_id = ts.transfer_status_id " +				
					"WHERE t.account_from = ? " + 
					"AND ts.transfer_status_desc NOT IN ('Approved', 'Rejected') " +
					"ORDER BY t.transfer_id";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
		while (results.next()) {
			transfers.add(mapRowToTransfer(results));
		}
		Transfer[] transfersArray = transfers.toArray(new Transfer[0]);
		return transfersArray;
	}
	
	
	@Override
	public Transfer[] viewTransferHistory(long id) {
		List<Transfer> transfers = new ArrayList<>(0);
		String sql = "SELECT " + 
				"        t.transfer_id, " + 
				"        t.amount, " + 
				"        tt.transfer_type_desc, " + 
				"        ts.transfer_status_desc, " + 
				"		 t.account_from," +
				"		 t.account_to, " +
				"        (CASE WHEN tt.transfer_type_id = (SELECT transfer_type_id FROM transfer_types WHERE transfer_type_desc = 'Send') AND u1.user_id = ? THEN 'To:' ELSE 'From:' END) as direction, " + 
				"        (CASE WHEN tt.transfer_type_id = (SELECT transfer_type_id FROM transfer_types WHERE transfer_type_desc = 'Send') AND u1.user_id = ? THEN u2.username ELSE u1.username END) as username, " + 
				"        (CASE WHEN tt.transfer_type_id = (SELECT transfer_type_id FROM transfer_types WHERE transfer_type_desc = 'Send') AND u1.user_id = ? THEN u2.user_id ELSE u1.user_id END) as user_id " + 
				"FROM transfers t " + 
				"        INNER JOIN accounts acc1 ON t.account_from = acc1.account_id " + 
				"                        INNER JOIN users u1 ON acc1.user_id = u1.user_id " + 
				"        INNER JOIN accounts acc2 ON t.account_to = acc2.account_id " + 
				"                        INNER JOIN users u2 ON acc2.user_id = u2.user_id " + 
				"        INNER JOIN transfer_types tt ON t.transfer_type_id = tt.transfer_type_id " + 
				"        INNER JOIN transfer_statuses ts ON t.transfer_status_id = ts.transfer_status_id " + 
				"WHERE " + 
				"acc1.user_id = ?" + 
				"OR " + 
				"acc2.user_id = ? " + 
				"ORDER BY t.transfer_id;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id, id, id, id, id);
		while (results.next()) {
			transfers.add(mapRowToTransfer(results));
		}
		Transfer[] transfersArray = transfers.toArray(new Transfer[0]);
		return transfersArray;
	}
	
	private Transfer mapRowToTransfer(SqlRowSet results) {
		Transfer transfer = new Transfer();
		transfer.setAccountFrom(results.getLong("account_from"));
		transfer.setAccountTo(results.getLong("account_to"));
		transfer.setAmount(results.getBigDecimal("amount"));
		transfer.setTransferId(results.getLong("transfer_id"));
		transfer.setTransferStatus(results.getString("transfer_status_desc"));
		transfer.setTransferType(results.getString("transfer_type_desc"));
		try {
			transfer.setOtherUser(results.getString("username"));
		}catch(InvalidResultSetAccessException e) {
			//eat the exception, this just means we didn't get back a username
		}
		try {
			transfer.setDirection(results.getString("direction"));
		}catch(InvalidResultSetAccessException e) {
			//eat the exception, this just means we didn't get back a direction
		}
		return transfer;
	}

	
	@Override
	public Transfer requestTransfer(Transfer transfer) {
		try {
			String sqlInsert = "INSERT INTO transfers (transfer_type_id, transfer_status_id, "
			+ "account_from, account_to, amount) VALUES "
			+ "(1, 1, ?, ?, ?) "
			+ "RETURNING transfer_id";
			long transferId = jdbcTemplate.queryForObject(sqlInsert, Long.class, transfer.getAccountFrom(), 
					transfer.getAccountTo(), transfer.getAmount().setScale(2));
			
			transfer.setTransferId(transferId);
			transfer.setTransferStatus("Pending");
			transfer.setAccountFromUsername(getUsernameFromAccountFrom(transfer.getTransferId()));
			transfer.setAccountToUsername(getUsernameFromAccountTo(transfer.getTransferId()));;
			
		}
		catch (DataAccessException e) {
			e.printStackTrace();
		}
		
		return transfer;
	}
	
	
	@Override
	public Transfer makeTransfer(Transfer transfer) {
		
		
		boolean successful = false;
		try {
//			jdbcTemplate.getDataSource().getConnection().setAutoCommit(false);
		
			String sqlInsert = "INSERT INTO transfers (transfer_type_id, transfer_status_id, "
					+ "account_from, account_to, amount) VALUES "
					+ "("
					+ "(SELECT transfer_type_id FROM transfer_types WHERE transfer_type_desc = ?), "
					+ "(SELECT transfer_status_id FROM transfer_statuses WHERE transfer_status_desc = ?), "
					+ "(SELECT account_id FROM accounts WHERE user_id = ?), "
					+ "(SELECT account_id FROM accounts WHERE user_id = ?), "
					+ "?) "
					+ "RETURNING transfer_id";
			Long transferId = jdbcTemplate.queryForObject(sqlInsert, Long.class, transfer.getTransferType(), 
					"Approved", transfer.getAccountFrom(), 
					transfer.getAccountTo(), transfer.getAmount().setScale(2));
			
			transfer.setTransferId(transferId);
			transfer.setTransferStatus("Approved");

		
			String sqlUpdateAccountFrom = 	"UPDATE accounts "
											+ "SET balance = (balance - ?) "
											+ "WHERE user_id = ?";
			jdbcTemplate.update(sqlUpdateAccountFrom, transfer.getAmount().setScale(2), transfer.getAccountFrom());

			
			String sqlUpdateAccountTo = 	"UPDATE accounts "
											+ "SET balance = (balance + ?) "
											+ "WHERE user_id = ?";
			jdbcTemplate.update(sqlUpdateAccountTo, transfer.getAmount().setScale(2), transfer.getAccountTo());
			
			
		
			successful = true;
//		} catch (SQLException e) {
//			e.printStackTrace();
//			successful = false;
		} catch (DataAccessException e) {
			e.printStackTrace();
			successful = false;
		}
		
		
//		try {
//			if (successful == false) {
//				jdbcTemplate.getDataSource().getConnection().rollback();
//				
//			} else {
//				jdbcTemplate.getDataSource().getConnection().commit();
//			}
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}
		
		return transfer;
	}
	
	public void approveTransfer(long id) {
		Transfer theTransfer = viewTransferDetails(id);
		
		if(theTransfer.getAmount().compareTo(viewCurrentBalance(theTransfer.getAccountFrom())) <= 0) {
			
			String sql = "UPDATE transfers "
					+ "SET transfer_status_id = (SELECT transfer_status_id FROM transfer_statuses WHERE transfer_status_desc = 'Approved') "
					+ "WHERE transfer_id = ?";
			
			jdbcTemplate.update(sql, id);
			
			String sqlUpdateAccountFrom = 	"UPDATE accounts "
					+ "SET balance = (balance - ?) "
					+ "WHERE account_id = ?";
			jdbcTemplate.update(sqlUpdateAccountFrom, theTransfer.getAmount(), theTransfer.getAccountFrom());
	
	
			String sqlUpdateAccountTo = 	"UPDATE accounts "
					+ "SET balance = (balance + ?) "
					+ "WHERE account_id = ?";
			jdbcTemplate.update(sqlUpdateAccountTo, theTransfer.getAmount(), theTransfer.getAccountTo());
		}
		
	}
	
	public void rejectTransfer(long id) {
		String sql = "UPDATE transfers "
				+ "SET transfer_status_id = (SELECT transfer_status_id FROM transfer_statuses WHERE transfer_status_desc = 'Rejected') "
				+ "WHERE transfer_id = ?";
		
		jdbcTemplate.update(sql, id);
	}
		
	@Override
	public Transfer viewTransferDetails(long id) {
		Transfer transfer = new Transfer();
		String sql = "SELECT " + 
				"        t.transfer_id, " + 
				"        t.amount, " + 
				"        tt.transfer_type_desc, " + 
				"        ts.transfer_status_desc, " + 
				"		 t.account_from," +
				"		 t.account_to, " +
				"		 u1.username as from_username, " +
				"		 u2.username as to_username " + 
				"FROM transfers t " + 
				"        INNER JOIN accounts acc1 ON t.account_from = acc1.account_id " + 
				"                        INNER JOIN users u1 ON acc1.user_id = u1.user_id " + 
				"        INNER JOIN accounts acc2 ON t.account_to = acc2.account_id " + 
				"                        INNER JOIN users u2 ON acc2.user_id = u2.user_id " + 
				"        INNER JOIN transfer_types tt ON t.transfer_type_id = tt.transfer_type_id " + 
				"        INNER JOIN transfer_statuses ts ON t.transfer_status_id = ts.transfer_status_id " + 
				"WHERE " + 
				"t.transfer_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
		while (results.next()) {
			transfer = mapRowToTransferDetails(results);
		}
		return transfer;
	}
	

	private Transfer mapRowToTransferDetails(SqlRowSet results) {
		Transfer transfer = new Transfer();
		transfer.setAccountFrom(results.getLong("account_from"));
		transfer.setAccountTo(results.getLong("account_to"));
		transfer.setAmount(results.getBigDecimal("amount"));
		transfer.setTransferId(results.getLong("transfer_id"));
		transfer.setTransferStatus(results.getString("transfer_status_desc"));
		transfer.setTransferType(results.getString("transfer_type_desc"));
		transfer.setAccountFromUsername(results.getString("from_username"));
		transfer.setAccountToUsername(results.getString("to_username"));
		return transfer;
	}

	private String getUsernameFromAccountFrom(long id) {
		String sql = "SELECT username u FROM users u "
				+ "INNER JOIN accounts a ON u.user_id = a.user_id "
				+ "INNER JOIN transfers t ON a.account_id = t.account_from "
				+ "WHERE t.transfer_id = ?";
		String userNameFrom = jdbcTemplate.queryForObject(sql, String.class, id);
		return userNameFrom;
	}
	
	private String getUsernameFromAccountTo(long id) {
		String sql = "SELECT username u FROM users u "
				+ "INNER JOIN accounts a ON u.user_id = a.user_id "
				+ "INNER JOIN transfers t ON a.account_id = t.account_to "
				+ "WHERE t.transfer_id = ?";
		String userNameTo = jdbcTemplate.queryForObject(sql, String.class, id);
		return userNameTo;
	}
}
