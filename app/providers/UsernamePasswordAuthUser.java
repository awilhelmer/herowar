package providers;

import play.Logger;

import com.feth.play.module.pa.user.NameIdentity;

@SuppressWarnings("serial")
public class UsernamePasswordAuthUser extends com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser implements NameIdentity {

  private final String name;

  public UsernamePasswordAuthUser(final FormSignup signup) {
    super(signup.password, signup.email);
    Logger.info("Username: " + signup.username + ", password: " + signup.password + ", email: " + signup.email);
    this.name = signup.username;
  }
  
  /**
   * Used for password reset only - do not use this to signup a user!
   * 
   * @param password
   */
  public UsernamePasswordAuthUser(final String password) {
    super(password, null);
    name = null;
  }

  @Override
  public String getName() {
    return name;
  }

}
