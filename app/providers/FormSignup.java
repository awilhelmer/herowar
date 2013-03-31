package providers;

import com.avaje.ebean.validation.Email;

/**
 * The Form is used for the signup process.
 * 
 * @author Sebastian Sachtleben
 */
public class FormSignup extends FormLogin {

  @Email
  private String email;
  
  @Override
  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Here should be validated that email and confirm email are the same ...
   * 
   * @return error message.
   */
  public String validate() {
    return null;
  }

}
