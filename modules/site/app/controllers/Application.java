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

  public static Result index() {
    return ok(index.render());
  }
  
  public static List<String> getJavascripts() {
    // TODO: this should be cached somewhere ...
    List<String> jsFiles = new ArrayList<String>();
    jsFiles.add("javascripts/ps" + BuildInfo.cacheNumber() + ".js");
    jsFiles.add("javascripts/pt" + BuildInfo.cacheNumber() + ".js");
    jsFiles.add("javascripts/pv" + BuildInfo.cacheNumber() + ".js");
    return jsFiles;
  }

}
