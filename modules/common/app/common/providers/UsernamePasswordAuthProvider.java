package common.providers;

import play.Application;
import play.data.Form;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.i18n.Messages;
import play.mvc.Call;
import play.mvc.Http.Context;

import com.avaje.ebean.validation.Email;
import com.feth.play.module.mail.Mailer.Mail.Body;

public class UsernamePasswordAuthProvider
    extends
    com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider<String, UsernamePasswordAuthUser, UsernamePasswordAuthUser, UsernamePasswordAuthProvider.MyLogin, UsernamePasswordAuthProvider.MySignup> {

  public UsernamePasswordAuthProvider(Application app) {
    super(app);
    // TODO Auto-generated constructor stub
  }

  public static class MyIdentity {

    public MyIdentity() {
    }

    public MyIdentity(final String email) {
      this.email = email;
    }

    @Required
    @Email
    public String email;

  }

  public static class MyLogin extends MyIdentity implements com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider.UsernamePassword {

    @Required
    @MinLength(5)
    public String password;

    @Override
    public String getEmail() {
      return email;
    }

    @Override
    public String getPassword() {
      return password;
    }
  }

  public static class MySignup extends MyLogin {

    @Required
    @MinLength(5)
    public String repeatPassword;

    @Required
    public String name;

    public String validate() {
      if (password == null || !password.equals(repeatPassword)) {
        return Messages.get("playauthenticate.password.signup.error.passwords_not_same");
      }
      return null;
    }
  }

  @Override
  protected UsernamePasswordAuthUser buildLoginAuthUser(MyLogin arg0, Context arg1) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected UsernamePasswordAuthUser buildSignupAuthUser(MySignup arg0, Context arg1) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected String generateVerificationRecord(UsernamePasswordAuthUser arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Form<MyLogin> getLoginForm() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Form<MySignup> getSignupForm() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Body getVerifyEmailMailingBody(String arg0, UsernamePasswordAuthUser arg1, Context arg2) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected String getVerifyEmailMailingSubject(UsernamePasswordAuthUser arg0, Context arg1) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider.LoginResult loginUser(UsernamePasswordAuthUser arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider.SignupResult signupUser(UsernamePasswordAuthUser arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected UsernamePasswordAuthUser transformAuthUser(UsernamePasswordAuthUser arg0, Context arg1) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Call userExists(com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Call userUnverified(com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser arg0) {
    // TODO Auto-generated method stub
    return null;
  }

}
