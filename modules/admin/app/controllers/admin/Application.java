package controllers.admin;

import info.BuildInfo;

import java.util.ArrayList;
import java.util.List;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.admin.index;

/**
 * The Application controller handles most requests for '/admin' pattern.
 * 
 * @author Sebastian Sachtleben
 */
public class Application extends Controller {

  private final static List<String> jsFiles = new ArrayList<String>();

  static {
    // TODO hardcoded prefix - Analyse from folder...
    jsFiles.add("javascripts/av" + BuildInfo.cacheNumber() + ".js");
    jsFiles.add("javascripts/as" + BuildInfo.cacheNumber() + ".js");
    jsFiles.add("javascripts/at" + BuildInfo.cacheNumber() + ".js");
  }

  public static Result index() {
    return ok(index.render());
  }
}
