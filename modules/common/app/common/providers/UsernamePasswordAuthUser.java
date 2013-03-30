package common.providers;

import com.feth.play.module.pa.user.NameIdentity;
import common.providers.UsernamePasswordAuthProvider.MySignup;

@SuppressWarnings("serial")
public class UsernamePasswordAuthUser extends com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser implements NameIdentity {

  private final String name;

  public UsernamePasswordAuthUser(final MySignup signup) {
    super(signup.password, signup.email);
    this.name = signup.name;
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
