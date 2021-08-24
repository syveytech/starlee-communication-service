package com.retail.starlee.models;

public class UserDetail {

	private int userId;
	private String userName;
	private String firstName;
	private String lastName;
	private String userMobileNumber;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserMobileNumber() {
		return userMobileNumber;
	}

	public void setUserMobileNumber(String userMobileNumber) {
		this.userMobileNumber = userMobileNumber;
	}

	@Override
	public String toString() {
		return "UserDetail [userId=" + userId + ", userName=" + userName
				+ ", firstName=" + firstName + ", lastName=" + lastName
				+ ", userMobileNumber=" + userMobileNumber + "]";
	}
}
