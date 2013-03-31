package providers;

import static play.data.Form.form;
import models.entity.LinkedAccount;
import models.entity.User;
import play.Application;
import play.Logger;
import play.data.Form;
import play.mvc.Call;
import play.mvc.Http.Context;

import com.feth.play.module.mail.Mailer.Mail.Body;

/**
 * The UsernamePasswordAuthProvider implementation handles our username and
 * password login. Unfortunatly our Play Auth plugin wants to login with email
 * and password so we changed some values to pass the username as email.
 * 
 * @author Sebastian Sachtleben
 */
public class UsernamePasswordAuthProvider
    extends
    com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider<String, LoginUsernamePasswordAuthUser, UsernamePasswordAuthUser, FormLogin, FormSignup> {

  public UsernamePasswordAuthProvider(Application app) {
    super(app);
  }

  public static final Form<FormSignup> SIGNUP_FORM = form(FormSignup.class);
  public static final Form<FormLogin> LOGIN_FORM = form(FormLogin.class);

  @Override
  protected LoginUsernamePasswordAuthUser buildLoginAuthUser(FormLogin login, Context ctx) {
    return new LoginUsernamePasswordAuthUser(login.getPassword(), login.getEmail());
  }

  @Override
  protected UsernamePasswordAuthUser buildSignupAuthUser(FormSignup signup, Context ctx) {
    return new UsernamePasswordAuthUser(signup);
  }

  @Override
  protected String generateVerificationRecord(UsernamePasswordAuthUser arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Form<FormLogin> getLoginForm() {
    return LOGIN_FORM;
  }

  @Override
  protected Form<FormSignup> getSignupForm() {
    return SIGNUP_FORM;
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
  protected LoginResult loginUser(LoginUsernamePasswordAuthUser authUser) {
    Logger.info("Login: " + authUser.toString());
    final User u = User.findByUsernamePasswordIdentity(authUser);
    if (u == null) {
      Logger.info("User not found");
      return LoginResult.NOT_FOUND;
    }
    for (final LinkedAccount acc : u.getLinkedAccounts()) {
      if (getKey().equals(acc.getProviderKey())) {
        if (authUser.checkPassword(acc.getProviderUserId(), authUser.getPassword())) {
          // Password was correct
          Logger.info("User is logged in");
          return LoginResult.USER_LOGGED_IN;
        } else {
          // if you don't return here,
          // you would allow the user to have
          // multiple passwords defined
          // usually we don't want this
          Logger.info("Passwort wrong 1");
          return LoginResult.WRONG_PASSWORD;
        }
      }
    }
    Logger.info("Passwort wrong 2");
    return LoginResult.WRONG_PASSWORD;
  }

  @Override
  protected SignupResult signupUser(UsernamePasswordAuthUser user) {
    final User u = User.findByUsernamePasswordIdentity(user);
    if (u != null) {
      return SignupResult.USER_EXISTS;
    }
    User.create(user);
    return SignupResult.USER_CREATED;
  }

  @Override
  protected LoginUsernamePasswordAuthUser transformAuthUser(UsernamePasswordAuthUser authUser, Context ctx) {
    return new LoginUsernamePasswordAuthUser(authUser.getEmail());
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

  @Override
  protected String onLoginUserNotFound(Context context) {
    Logger.warn("User not found");
    return null;
  }
}
