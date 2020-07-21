package com.techelevator.tenmo.models;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import io.cucumber.java.lu.a.as;

public class TransferTest {

	private Transfer testTransfer;
	
	@Before
	public void setup() {
		testTransfer = new Transfer();
	}
	
	
	@Test
	public void test_set_and_get_transfer_id() {
		long transferId = 123;
		testTransfer.setTransferId(transferId);
		assertEquals(transferId, testTransfer.getTransferId());
	}
	
	@Test
	public void test_set_and_get_transfer_type() {
		String transferType = "Abc";
		testTransfer.setTransferType(transferType);
		assertEquals(transferType, testTransfer.getTransferType());
	}
	
	@Test
	public void test_set_and_get_transfer_status() {
		String transferStatus = "Abc";
		testTransfer.setTransferStatus(transferStatus);
		assertEquals(transferStatus, testTransfer.getTransferStatus());
	}
	
	@Test
	public void test_set_and_get_account_from_id() {
		long accountFromId = 1234;
		testTransfer.setAccountFrom(accountFromId);
		assertEquals(accountFromId, testTransfer.getAccountFrom());
	}
	
	@Test
	public void test_set_and_get_account_to_id() {
		long accountToId = 1234;
		testTransfer.setAccountTo(accountToId);
		assertEquals(accountToId, testTransfer.getAccountTo());
	}
	
	@Test
	public void test_set_and_get_amount() {
		BigDecimal amount = BigDecimal.valueOf(100.00).setScale(2);
		testTransfer.setAmount(amount);
		assertEquals(amount, testTransfer.getAmount());
	}
	
	@Test
	public void test_set_and_get_other_user() {
		String otherUsername = "testperson";
		testTransfer.setOtherUser(otherUsername);
		assertEquals(otherUsername, testTransfer.getOtherUser());
	}
	
	@Test
	public void test_set_and_get_direction() {
		String direction = "To:";
		testTransfer.setDirection(direction);
		assertEquals(direction, testTransfer.getDirection());
	}
	
	@Test
	public void test_set_and_get_account_from_username() {
		String accountFromUsername = "testperson";
		testTransfer.setAccountFromUsername(accountFromUsername);
		assertEquals(accountFromUsername, testTransfer.getAccountFromUsername());
	}
	
	@Test
	public void test_set_and_get_account_to_username() {
		String accountToUsername = "testperson";
		testTransfer.setAccountToUsername(accountToUsername);
		assertEquals(accountToUsername, testTransfer.getAccountToUsername());
	}
	

}
