package providers;

import com.feth.play.module.pa.user.NameIdentity;

@SuppressWarnings("serial")
public class SignupUsernamePasswordAuthUser extends com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser implements NameIdentity {

  private final String name;

  public SignupUsernamePasswordAuthUser(final FormSignup signup) {
    super(signup.getPassword(), signup.getEmail());
    this.name = signup.getUsername();
  }
  
  /**
   * Used for password reset only - do not use this to signup a user!
   * 
   * @param password
   */
  public SignupUsernamePasswordAuthUser(final String password) {
    super(password, null);
    name = null;
  }

  @Override
  public String getName() {
    return name;
  }

}
