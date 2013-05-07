package models.api.error;

import models.api.ApiError;

public class NotLoggedInError extends ApiError {

  public NotLoggedInError() {
    super(80025, "Not logged in", "Please login to do this action.");
  }

}
