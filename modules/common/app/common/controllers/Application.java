package common.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import play.mvc.Controller;
import play.mvc.Http.Session;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.user.AuthUser;

import common.models.entity.User;

public class Application extends Controller {

  public static final String FLASH_MESSAGE_KEY = "message";
  public static final String FLASH_ERROR_KEY = "error";
  public static final String USER_ROLE = "user";

  public static User getLocalUser(final Session session) {
    final AuthUser currentAuthUser = PlayAuthenticate.getUser(session);
    final User localUser = User.findByAuthUserIdentity(currentAuthUser);
    return localUser;
  }

  public static String formatTimestamp(final long t) {
    return new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(new Date(t));
  }

}
