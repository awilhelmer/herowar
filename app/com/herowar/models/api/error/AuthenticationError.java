package com.herowar.models.api.error;

import com.herowar.models.api.ApiError;

public class AuthenticationError extends ApiError {

	public AuthenticationError() {
		super(80010, "Username/Password is incorrect", "This error can be caused by an incorrect username or password.");
	}

}
