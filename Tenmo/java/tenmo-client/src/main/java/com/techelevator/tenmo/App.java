package com.techelevator.tenmo;

import java.math.BigDecimal;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.TransferServiceException;
import com.techelevator.view.ConsoleService;

public class App {

	private static final String API_BASE_URL = "http://localhost:8080/";

	private static final String MENU_OPTION_EXIT = "Exit";
	private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN,
			MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String PENDING_REQUESTS_APPROVE = "1: Approve";
	private static final String PENDING_REQUESTS_REJECT = "2: Reject";
	private static final String PENDING_REQUESTS_CANCEL = "0: Don't approve or reject";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS,
			MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS,
			MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String[] PENDING_MENU_OPTIONS = { PENDING_REQUESTS_APPROVE, 
			PENDING_REQUESTS_REJECT, PENDING_REQUESTS_CANCEL };
			
	private AuthenticatedUser currentUser;
	private ConsoleService console;
	private AuthenticationService authenticationService;
	private TransferService transferService;

	public static void main(String[] args) {
		App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL),
				new TransferService(API_BASE_URL));
		app.run();
	}

	public App(ConsoleService console, AuthenticationService authenticationService, TransferService transferService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.transferService = transferService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");

		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while (true) {
			String choice = (String) console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if (MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
//		console.getUserInput(transferService.getBalance(currentUser.getUser()).toString());
		String balanceText = "";
		try {
			balanceText = transferService.getBalance(currentUser).setScale(2).toString() + " TE Bucks";
		} catch (TransferServiceException e) {
			console.println(e.getMessage());
			return;
		}
		console.println("====================================");
		console.println("  Your current account balance is: ");
		console.println("------------------------------------");
		console.println("         " + balanceText);
		console.println("====================================");
		console.getUserInput("Press Enter to continue");
	}

	private void viewTransferHistory() {
		try {
			console.println(Transfer.toStringHeader());
			console.displayUserIdsForTransfer(transferService.viewTransferHistory(currentUser));
			console.println("----------");
			int id = console.getUserInputInteger("Please enter transfer ID to view details (0 to cancel)");
			if (id == 0) {
				return;
			} else {
				console.println("");
				console.println("--------------------------------------------");
				console.println("Transfer Details");
				console.println("--------------------------------------------");
				console.println(transferService.viewTransferDetails(currentUser, id).transferDetails());
				console.println("----------");
				console.getUserInput("Press Enter to continue");
			}
		} catch (TransferServiceException e) {

			e.printStackTrace();
		}

	}

	private void viewPendingRequests() {
		try {
			console.println("-------------------------------------------");
			console.println("- - - - - - Pending Transfers - - - - - - -");
			console.println("ID		To		Amount");
			console.println("-------------------------------------------");
			
			for(Transfer t : transferService.viewPendingRequests(currentUser)){
				console.println(t.pendingTransferToString());
			}
			int id = console.getUserInputInteger("Please enter transfer ID to approve or reject (0 to cancel)");
			if (id == 0) {
				return;
			} else {
				console.displayUserIdsForTransfer(PENDING_MENU_OPTIONS);
				int selection = console.getUserInputInteger("Please choose an option");
				if (selection == 1) {
					transferService.approveTransfer(currentUser, id);
				} else if (selection == 2) {
					transferService.rejectTransfer(currentUser, id);
				} else {
					return;
				}
				
				console.println(transferService.viewTransferDetails(currentUser, id).transferDetails());
				console.println("----------");
				console.getUserInput("Press Enter to continue");
			}
		}
		catch (TransferServiceException e) {

			e.printStackTrace();
		}
	}

	private void sendBucks() {
		Transfer transfer = new Transfer();
		transfer.setAccountFrom(currentUser.getUser().getId());
		try {
			console.println(User.toStringHeader());
			console.displayUserIdsForTransfer(transferService.listUsers(currentUser));
			console.println("----------");
			int id = console.getUserInputInteger("Enter ID of user you are sending to (0 to cancel)");
			if (id == 0) {
				return;
			}
			transfer.setAccountTo(id);
			BigDecimal amount = BigDecimal.valueOf(console.getUserInputDouble(
					"Enter amount (current balance is: $" + transferService.getBalance(currentUser).setScale(2) + ")"));
			transfer.setAmount(amount);
			transfer.setTransferType("Send");

			transfer = transferService.makeTransfer(transfer, currentUser);
			if (transfer == null) {
				console.println("\n   You do not have enough TE bucks in your account to complete this request.");
			} else {
				console.println("");
				console.println(
						"Transfer was successful! Please keep this ID for your records: " + transfer.getTransferId());
				console.println("");
				console.println("Your remaining balance is: $" + transferService.getBalance(currentUser).setScale(2));
				console.getUserInput("Press Enter to continue");
			}
		} catch (TransferServiceException e) {
			console.println(e.getMessage());
			return;
		}

	}

	private void requestBucks() {
		Transfer transfer = new Transfer();
		transfer.setAccountTo(currentUser.getUser().getId());
		try {
			console.println(User.toStringHeader());
			console.displayUserIdsForTransfer(transferService.listUsers(currentUser));
			console.println("----------");
			int id = console.getUserInputInteger("Enter ID of user you are requesting TE Bucks from (0 to cancel)");
			if (id == 0) {
				return;
			}
			transfer.setAccountFrom(id);
			BigDecimal amount = BigDecimal.valueOf(console.getUserInputDouble("Enter amount to request"));
			transfer.setAmount(amount);
			transfer.setTransferType("Request");

			transfer = transferService.requestTransfer(transfer, currentUser);

			console.println("\nRequesting that " + transfer.getAccountFromUsername() + " send $"
					+ transfer.getAmount().setScale(2) + " to your account.\n" + transfer.getAccountFromUsername()
					+ " must approve this request before the transfer is complete.");
			console.getUserInput("\nPress Enter to continue");

		} catch (TransferServiceException e) {
			console.println(e.getMessage());
			return;
		}
	}

	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while (!isAuthenticated()) {
			String choice = (String) console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
		while (!isRegistered) // will keep looping until user is registered
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				authenticationService.register(credentials);
				isRegistered = true;
				System.out.println("Registration successful. You can now login.");
			} catch (AuthenticationServiceException e) {
				System.out.println("REGISTRATION ERROR: " + e.getMessage());
				System.out.println("Please attempt to register again.");
			}
		}
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) // will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: " + e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}

	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
