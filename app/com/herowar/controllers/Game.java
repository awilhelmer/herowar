package com.herowar.controllers;


import org.codehaus.jackson.JsonNode;

import com.herowar.models.entity.User;
import com.herowar.network.Connection;

import play.Logger;
import play.db.jpa.JPA;
import play.libs.F.Function0;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.game.index;

/**
 * Provides all game related routes.
 * 
 * @author Sebastian Sachtleben
 */
public class Game extends Controller {
	private static final Logger.ALogger log = Logger.of(Game.class);

	/**
	 * Render game index view.
	 * <p>
	 * The game itself is a single page application and don't need additional routes.
	 * </p>
	 * 
	 * @return The {@link Result} object.
	 */
	public static Result index() {
		return ok(index.render());
	}

	/**
	 * Provides socket connection for exchanging game data.
	 * <p>
	 * This {@link play.mvc.WebSocket} only allow to send and receive {@link JsonNode} objects. Any other object type will result discoonect
	 * the connection.
	 * </p>
	 * 
	 * @return The {@link play.mvc.WebSocket} object.
	 */
	public static play.mvc.WebSocket<JsonNode> data() {
		try {
			return JPA.withTransaction(new Function0<play.mvc.WebSocket<JsonNode>>() {
				@Override
				public play.mvc.WebSocket<JsonNode> apply() throws Throwable {
					final User user = Application.getLocalUser(session());
					return new Connection(request().remoteAddress(), user);
				}
			});
		} catch (Throwable e) {
			log.error("Exception occured during creation of websocket", e);
		}
		return null;
	}

}
