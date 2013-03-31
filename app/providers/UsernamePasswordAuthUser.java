package providers;

import providers.UsernamePasswordAuthProvider.SignupForm;

import com.feth.play.module.pa.user.NameIdentity;

@SuppressWarnings("serial")
public class UsernamePasswordAuthUser extends com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser implements NameIdentity {

  private final String name;

  public UsernamePasswordAuthUser(final SignupForm signup) {
    super(signup.password, signup.email);
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
