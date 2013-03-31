package providers;

import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;

public class Identity {

  public Identity() {
  }

  public Identity(final String username) {
    this.username = username;
  }

  public String email;

  @Required
  @MinLength(5)
  public String username;

}
