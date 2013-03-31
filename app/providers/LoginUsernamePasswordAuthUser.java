package providers;

import com.feth.play.module.pa.providers.password.DefaultUsernamePasswordAuthUser;

@SuppressWarnings("serial")
public class LoginUsernamePasswordAuthUser extends DefaultUsernamePasswordAuthUser {

  /**
   * The session timeout in seconds Defaults to two weeks
   */
  final static long SESSION_TIMEOUT = 24 * 14 * 3600;
  private long expiration;

  /**
   * For logging the user in automatically
   * 
   * @param email
   */
  public LoginUsernamePasswordAuthUser(final String email) {
    this(null, email);
  }

  public LoginUsernamePasswordAuthUser(final String clearPassword, final String email) {
    super(clearPassword, email);
    expiration = System.currentTimeMillis() + 1000 * SESSION_TIMEOUT;
  }

  @Override
  public long expires() {
    return expiration;
  }

}
