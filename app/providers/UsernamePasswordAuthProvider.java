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
    com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider<String, LoginUsernamePasswordAuthUser, SignupUsernamePasswordAuthUser, FormLogin, FormSignup> {

  public UsernamePasswordAuthProvider(Application app) {
    super(app);
  }

  public static final Form<FormSignup> SIGNUP_FORM = form(FormSignup.class);
  public static final Form<FormLogin> LOGIN_FORM = form(FormLogin.class);

  @Override
  protected Form<FormLogin> getLoginForm() {
    return LOGIN_FORM;
  }

  @Override
  protected Form<FormSignup> getSignupForm() {
    return SIGNUP_FORM;
  }
  
  @Override
  protected LoginUsernamePasswordAuthUser buildLoginAuthUser(FormLogin login, Context ctx) {
    // We need to fetch users email here since we login with username instead email
    User u = User.getFinder().where().eq("username", login.getEmail()).findUnique();
    return new LoginUsernamePasswordAuthUser(login.getPassword(), u.getEmail());
  }

  @Override
  protected SignupUsernamePasswordAuthUser buildSignupAuthUser(FormSignup signup, Context ctx) {
    return new SignupUsernamePasswordAuthUser(signup);
  }

  @Override
  protected LoginResult loginUser(LoginUsernamePasswordAuthUser authUser) {
    final User u = User.findByUsernamePasswordIdentity(authUser);
    if (u == null) {
      Logger.info("User " + authUser.getEmail() + " not found");
      return LoginResult.NOT_FOUND;
    }
    for (final LinkedAccount acc : u.getLinkedAccounts()) {
      if (getKey().equals(acc.getProviderKey())) {
        if (authUser.checkPassword(acc.getProviderUserId(), authUser.getPassword())) {
          Logger.info("User " + authUser.getEmail() + " is logged in");
          return LoginResult.USER_LOGGED_IN;
        } else {
          return LoginResult.WRONG_PASSWORD;
        }
      }
    }
    return LoginResult.WRONG_PASSWORD;
  }

  @Override
  protected SignupResult signupUser(SignupUsernamePasswordAuthUser user) {
    final User u = User.findByUsernamePasswordIdentity(user);
    if (u != null) {
      return SignupResult.USER_EXISTS;
    }
    User.create(user);
    return SignupResult.USER_CREATED;
  }
  
  @Override
  protected LoginUsernamePasswordAuthUser transformAuthUser(SignupUsernamePasswordAuthUser authUser, Context ctx) {
    return new LoginUsernamePasswordAuthUser(authUser.getEmail());
  }
  
  @Override
  protected String generateVerificationRecord(SignupUsernamePasswordAuthUser arg0) {
    return null;
  }
  
  @Override
  protected Body getVerifyEmailMailingBody(String arg0, SignupUsernamePasswordAuthUser arg1, Context arg2) {
    return null;
  }

  @Override
  protected String getVerifyEmailMailingSubject(SignupUsernamePasswordAuthUser arg0, Context arg1) {
    return null;
  }

  @Override
  protected Call userExists(com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser arg0) {
    return null;
  }

  @Override
  protected Call userUnverified(com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser arg0) {
    return null;
  }

  @Override
  protected String onLoginUserNotFound(Context context) {
    return null;
  }
}
