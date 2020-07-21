package com.techelevator.tenmo.services;

import java.math.BigDecimal;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;

public class TransferService {

	public static String AUTH_TOKEN = "";
	private String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();
	
	public TransferService(String url) {
		this.BASE_URL = url;
	}
	
	public BigDecimal getBalance(AuthenticatedUser user) throws TransferServiceException {
		BigDecimal balance = null;
		try {
			balance = restTemplate.exchange(BASE_URL + "/account/" + user.getUser().getId(), HttpMethod.GET, makeAuthEntity(user), BigDecimal.class).getBody();	
		} catch (RestClientResponseException e) {
			throw new TransferServiceException(e.getMessage());
		}
		return balance;
	}
	
	public User[] listUsers(AuthenticatedUser user) throws TransferServiceException {
		User[] users = null;
		try {
			users = restTemplate.exchange(BASE_URL + "/users", HttpMethod.GET, makeAuthEntity(user), User[].class).getBody();
		} catch (RestClientResponseException e) {
			throw new TransferServiceException(e.getMessage());
		}
		return users;
	}
	
	public Transfer makeTransfer(Transfer transfer, AuthenticatedUser user) throws TransferServiceException {
		Transfer theTransfer = new Transfer();
		try {
			theTransfer = restTemplate.exchange(BASE_URL + "/transfer", HttpMethod.POST, makeTransferEntity(transfer, user), Transfer.class).getBody();
		} catch (RestClientResponseException e) {
			throw new TransferServiceException(e.getMessage());
		}
		return theTransfer;
	}
	
	public Transfer requestTransfer(Transfer transfer, AuthenticatedUser user) throws TransferServiceException {
		Transfer theTransfer = new Transfer();
		try {
			theTransfer = restTemplate.exchange(BASE_URL + "/request", HttpMethod.POST, makeTransferEntity(transfer, user), Transfer.class).getBody();
		} catch (RestClientResponseException e) {
			throw new TransferServiceException(e.getMessage());
		}
		return theTransfer;
	}
	
	public Transfer[] viewPendingRequests(AuthenticatedUser user) throws TransferServiceException {
		Transfer[] transfers = null;
		try {
			transfers = restTemplate.exchange(BASE_URL + "/request/" + user.getUser().getId(), HttpMethod.GET, makeAuthEntity(user), Transfer[].class).getBody();
		} catch (RestClientResponseException e) {
			throw new TransferServiceException(e.getMessage());
		}
		return transfers;
	}
	
	public void approveTransfer(AuthenticatedUser user, long id) throws TransferServiceException{
		try {
			restTemplate.exchange(BASE_URL + "/request/" + id + "/approve", HttpMethod.PUT, makeAuthEntity(user), Void.class );
		}catch(RestClientResponseException e) {
			throw new TransferServiceException(e.getMessage());
		}
	}
	
	public void rejectTransfer(AuthenticatedUser user, long id) throws TransferServiceException{
		try {
			restTemplate.exchange(BASE_URL + "/request/" + id + "/reject", HttpMethod.PUT, makeAuthEntity(user), Void.class );
		}catch(RestClientResponseException e) {
			throw new TransferServiceException(e.getMessage());
		}
	}
	
	public Transfer[] viewTransferHistory(AuthenticatedUser user) throws TransferServiceException {
		Transfer[] transfers = null;
		try {
			transfers = restTemplate.exchange(BASE_URL + "/transfer/" + user.getUser().getId(), HttpMethod.GET, makeAuthEntity(user), Transfer[].class).getBody();
		}catch (RestClientResponseException e) {
			throw new TransferServiceException(e.getMessage());
		}
		return transfers;
	}
	
	public Transfer viewTransferDetails(AuthenticatedUser user, long id) throws TransferServiceException {
		Transfer transfer = new Transfer();
		try {
			transfer = restTemplate.exchange(BASE_URL + "/transfer/" + id + "/details", HttpMethod.GET, makeAuthEntity(user), Transfer.class).getBody();
		} catch (RestClientResponseException e) {
			throw new TransferServiceException(e.getMessage());
		}
		return transfer;
	}
	
	private HttpEntity makeAuthEntity(AuthenticatedUser user){
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(user.getToken());
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}
	
	private HttpEntity<Transfer> makeTransferEntity(Transfer transfer, AuthenticatedUser user){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(user.getToken());
		HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
		return entity;
	}
}
