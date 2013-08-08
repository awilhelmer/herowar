package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

/**
 * The Application controller handles all requests for our site and provides js file list for site and admin pages.
 * 
 * @author Sebastian Sachtleben
 */
public class Application extends Controller {

	public static Result index() {
		return ok(index.render());
	}

}
