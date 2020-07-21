package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Transfer {

	private long transferId;
	private String transferType;
	private String transferStatus;
	private long accountFrom;
	private long accountTo;
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

	public static String toStringHeader() {
		String headerString = "--------------------------------------------\n";
		headerString += "Transfer\n";
		headerString += String.format("%-10s %-21s %-10s\n", "ID", 
				"From/To", "Amount");
		headerString += "--------------------------------------------";
		
		return headerString;
	}
	
	@Override
	public String toString() {
		return String.format("%-10s %-5s %-15s $%10s", this.getTransferId(), 
				this.getDirection(), this.getOtherUser(), this.getAmount().setScale(2));
	}
	
	public String pendingTransferToString() {
		return String.format("%-10s %-5s %-15s $%10s", this.getTransferId(), 
				"To:", this.getOtherUser(), this.getAmount().setScale(2));
	}
	
	public String transferDetails() {
		return String.format("%-7s %s \n"
				+ "%-7s %s \n"
				+ "%-7s %s \n"
				+ "%-7s %s \n"
				+ "%-7s %s \n"
				+ "%-7s $%s",
				"Id:", this.getTransferId(),
				"From:", this.getAccountFromUsername(),
				"To:", this.getAccountToUsername(),
				"Type:", this.getTransferType(),
				"Status:", this.getTransferStatus(),
				"Amount:", this.getAmount()
				);
	}
	
}
