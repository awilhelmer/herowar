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
 * The Application controller handles all requests for our site and provides js file list for site and admin pages.
 * 
 * @author Sebastian Sachtleben
 */
public class Application extends Controller {
  
  public static final String FLASH_MESSAGE_KEY = "message";
  public static final String FLASH_ERROR_KEY = "error";
  
  public static final String USER_ROLE = "user";
  public static final String ADMIN_ROLE = "admin";
  
  private final static List<String> jsFilesAdmin = new ArrayList<String>();
  private final static List<String> jsFilesSite = new ArrayList<String>();
  private final static List<String> jsFilesGame = new ArrayList<String>();
  static {
    //TODO hardcoded prefix - Analyse from folder... 
    jsFilesAdmin.add("javascripts/av" + BuildInfo.cacheNumber() + ".js");
    jsFilesAdmin.add("javascripts/as" + BuildInfo.cacheNumber() + ".js");
    jsFilesAdmin.add("javascripts/at" + BuildInfo.cacheNumber() + ".js");
    jsFilesSite.add("javascripts/sv" + BuildInfo.cacheNumber() + ".js");
    jsFilesSite.add("javascripts/ss" + BuildInfo.cacheNumber() + ".js");
    jsFilesSite.add("javascripts/st" + BuildInfo.cacheNumber() + ".js");
    jsFilesGame.add("javascripts/gv" + BuildInfo.cacheNumber() + ".js");
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

  public static List<String> getJsFilesSite() {
    return jsFilesSite;
  }
  
  public static List<String> getJsFilesAdmin() {
    return jsFilesAdmin;
  }

  public static List<String> getJsFilesGame() {
    return jsFilesGame;
  }

}
