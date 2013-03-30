package controllers;

import info.BuildInfo;

import java.util.ArrayList;
import java.util.List;

import play.mvc.Controller;
import play.mvc.Result;

import views.html.index;

/**
 * The Application controller handles request for "/" pattern.
 * 
 * @author Sebastian Sachtleben
 */
public class Application extends Controller {
  private final static List<String> jsFiles = new ArrayList<String>();
  static {
    //TODO hardcoded prefix - Analyse from folder... 
    jsFiles.add("javascripts/sv" + BuildInfo.cacheNumber() + ".js");
    jsFiles.add("javascripts/ss" + BuildInfo.cacheNumber() + ".js");
    jsFiles.add("javascripts/st" + BuildInfo.cacheNumber() + ".js");
  }
  
  public static Result index() {
    return ok(index.render());
  }
  
  public static Result signup() {
    return ok(index.render());
  }

  public static List<String> getJavascripts() {
    return jsFiles;
  }

}
