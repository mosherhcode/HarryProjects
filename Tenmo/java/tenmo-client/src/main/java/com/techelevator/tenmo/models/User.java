package com.techelevator.tenmo.models;

public class User {

	private Integer id;
	private String username;

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public static String toStringHeader() {
		String headerString = "--------------------------------------------\n";
		headerString += String.format("%-11s %s\n", "User's ID", 
				"Name");
		headerString += "--------------------------------------------";
		
		return headerString;
	}
	
	@Override
	public String toString() {
		return String.format("%-11s %s", this.id, this.username);
	}
}
