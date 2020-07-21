package com.techelevator.tenmo.controller;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountTransferDAO;
import com.techelevator.tenmo.model.Transfer;

@PreAuthorize("isAuthenticated()")
@RestController
public class TransferController {

	private AccountTransferDAO accountTransferDAO; 
	
	public TransferController(AccountTransferDAO accountTransferDAO) {
		this.accountTransferDAO = accountTransferDAO;
	}
	
	@RequestMapping(path = "/account/{id}", method = RequestMethod.GET)
	public BigDecimal getbalance(@PathVariable long id) {
		return accountTransferDAO.viewCurrentBalance(id);
	}
	
	@RequestMapping(path = "/transfer", method = RequestMethod.POST)
	public Transfer makeTransfer(@Valid @RequestBody Transfer transfer) {
		if (accountTransferDAO.viewCurrentBalance(transfer.getAccountFrom()).compareTo(transfer.getAmount()) >= 0) {
			return accountTransferDAO.makeTransfer(transfer);			
		} return null;
	}
	
	@RequestMapping(path = "/request", method = RequestMethod.POST)
	public Transfer requestTransfer(@Valid @RequestBody Transfer transfer) {
		return accountTransferDAO.requestTransfer(transfer);			
	}
	
	@RequestMapping(path = "/request/{id}", method = RequestMethod.GET)
	public Transfer[] viewPendingRequests(@PathVariable long id) {
		return accountTransferDAO.viewPendingRequests(id);			
	}
	
	@RequestMapping(path = "/request/{id}/approve", method = RequestMethod.PUT)
	public void approveRequest(@PathVariable long id) {
		accountTransferDAO.approveTransfer(id);
	}
	
	@RequestMapping(path = "/request/{id}/reject", method = RequestMethod.PUT)
	public void rejectRequest(@PathVariable long id) {
		accountTransferDAO.rejectTransfer(id);
	}
	
	@RequestMapping(path = "/transfer/{id}", method = RequestMethod.GET)
	public Transfer[] viewTransferHistory(@PathVariable long id) {
		return accountTransferDAO.viewTransferHistory(id);
	}
	
	@RequestMapping(path = "/transfer/{id}/details", method = RequestMethod.GET)
	public Transfer viewTransferDetails(@PathVariable long id) {
		return accountTransferDAO.viewTransferDetails(id);
	}

}
