package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.game.index;

public class Game extends Controller {

	public static Result index() {
		return ok(index.render());
	}

	public static Result test() {
		return ok(index.render());
	}
}
