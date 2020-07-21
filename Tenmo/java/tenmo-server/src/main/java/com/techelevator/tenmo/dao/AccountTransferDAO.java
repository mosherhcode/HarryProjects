package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

import com.techelevator.tenmo.model.Transfer;

public interface AccountTransferDAO {


	// methods
	
	public BigDecimal viewCurrentBalance(long id);
	
	public Transfer makeTransfer(Transfer transfer);	
	
	public Transfer[] viewTransferHistory(long id);
	
	public Transfer viewTransferDetails(long id);
	
	public Transfer requestTransfer(Transfer transfer);
	
	public Transfer[] viewPendingRequests(long id);
	
	public void approveTransfer(long id);
	
	public void rejectTransfer(long id);
	
}
