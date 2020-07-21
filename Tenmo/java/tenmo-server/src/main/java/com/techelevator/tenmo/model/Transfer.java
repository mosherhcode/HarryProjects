package com.techelevator.tenmo.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class Transfer {

	private long transferId;
	private String transferType;
	private String transferStatus;
	@NotNull(message="From user cannot be null")
	private long accountFrom;
	@NotNull(message="To user cannot be null")
	private long accountTo;
	@Positive(message="amount cannot be negetive")
	private BigDecimal amount;
	private String otherUser;
	private String direction;

	private String accountFromUsername;
	
	private String accountToUsername;
	
	public Transfer() {
	}
	
	public long getTransferId() {
		return transferId;
	}

	public void setTransferId(long transferId) {
		this.transferId = transferId;
	}

	public String getTransferType() {
		return transferType;
	}

	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

	public String getTransferStatus() {
		return transferStatus;
	}

	public void setTransferStatus(String transferStatus) {
		this.transferStatus = transferStatus;
	}

	public long getAccountFrom() {
		return accountFrom;
	}

	public void setAccountFrom(long accountFrom) {
		this.accountFrom = accountFrom;
	}

	public long getAccountTo() {
		return accountTo;
	}

	public void setAccountTo(long accountTo) {
		this.accountTo = accountTo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getOtherUser() {
		return otherUser;
	}

	public void setOtherUser(String otherUser) {
		this.otherUser = otherUser;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getAccountFromUsername() {
		return accountFromUsername;
	}

	public void setAccountFromUsername(String accountFromUsername) {
		this.accountFromUsername = accountFromUsername;
	}

	public String getAccountToUsername() {
		return accountToUsername;
	}

	public void setAccountToUsername(String accountToUsername) {
		this.accountToUsername = accountToUsername;
	}

}
