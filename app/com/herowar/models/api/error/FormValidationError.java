package com.herowar.models.api.error;

import com.herowar.models.api.ApiError;

public class FormValidationError extends ApiError {

	public FormValidationError(Object body) {
		super(80000, "Form validation failed",
				"The form validation failed for some reason, more detailed information will be send in body object.", body);
	}

}
