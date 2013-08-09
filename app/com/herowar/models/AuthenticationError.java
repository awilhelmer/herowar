package com.herowar.models;


public class AuthenticationError extends ApiError {

	public AuthenticationError() {
		super(80010, "Username/Password is incorrect", "This error can be caused by an incorrect username or password.");
	}

}
