package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import views.html.game.index;

/**
 * Provides all game related routes.
 * 
 * @author Sebastian Sachtleben
 */
public class Game extends Controller {

	public static Result index() {
		return ok(index.render());
	}

	public static Result test() {
		return ok(index.render());
	}

	public static WebSocket<String> socket() {
		return new WebSocket<String>() {
			@Override
			public void onReady(play.mvc.WebSocket.In<String> in, play.mvc.WebSocket.Out<String> out) {
				// TODO Auto-generated method stub

			}
		};
	}
}
