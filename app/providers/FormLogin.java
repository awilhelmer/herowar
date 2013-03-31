package providers;

import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider.UsernamePassword;

public class FormLogin extends Identity implements UsernamePassword {

  @Required
  @MinLength(5)
  public String password;

  @Override
  public String getEmail() {
    return username;
  }

  @Override
  public String getPassword() {
    return password;
  }

}
