package providers;

import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider.UsernamePassword;

/**
 * The Form is used for the login process.
 * 
 * @author Sebastian Sachtleben
 */
public class FormLogin implements UsernamePassword {

  @Required
  @MinLength(5)
  protected String username;
  
  @Required
  @MinLength(5)
  protected String password;

  /**
   * Attention: We return here username since the user should login with username and password rather than email.
   */
  @Override
  public String getEmail() {
    return username;
  }
  
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
