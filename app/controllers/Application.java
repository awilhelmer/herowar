package controllers;

import info.BuildInfo;

import java.util.ArrayList;
import java.util.List;

import models.entity.User;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.user.AuthUser;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.Session;

import views.html.index;

/**
 * The Application controller handles request for "/" pattern.
 * 
 * @author Sebastian Sachtleben
 */
public class Application extends Controller {
  
  public static final String FLASH_MESSAGE_KEY = "message";
  public static final String FLASH_ERROR_KEY = "error";
  
  public static final String USER_ROLE = "user";
  public static final String ADMIN_ROLE = "admin";
  
  private final static List<String> jsFiles = new ArrayList<String>();
  
  static {
    //TODO hardcoded prefix - Analyse from folder... 
    jsFiles.add("javascripts/sv" + BuildInfo.cacheNumber() + ".js");
    jsFiles.add("javascripts/ss" + BuildInfo.cacheNumber() + ".js");
    jsFiles.add("javascripts/st" + BuildInfo.cacheNumber() + ".js");
  }
  
  public static User getLocalUser(final Session session) {
    final AuthUser currentAuthUser = PlayAuthenticate.getUser(session);
    final User localUser = User.findByAuthUserIdentity(currentAuthUser);
    return localUser;
  }
  
  public static Result index() {
    return ok(index.render());
  }
  
  public static Result signup() {
    return ok(index.render());
  }
  
  public static Result goPlay() {
    return ok(index.render());
  }

  public static List<String> getJavascripts() {
    return jsFiles;
  }

}
