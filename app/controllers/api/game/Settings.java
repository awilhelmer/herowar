package controllers.api.game;

import static play.libs.Json.toJson;

import java.lang.reflect.InvocationTargetException;

import models.entity.User;
import models.entity.game.PlayerSettings;
import play.db.jpa.Transactional;
import play.mvc.Result;
import controllers.Application;
import controllers.api.BaseAPI;

/**
 * The Settings controller handle api requests for the PlayerSettings model of the current logged in user.
 * 
 * @author Sebastian Sachtleben
 */
public class Settings extends BaseAPI<Long, PlayerSettings> {

	private Settings() {
		super(Long.class, PlayerSettings.class);
	}

	public static final Settings instance = new Settings();

	@Transactional
	public static Result show() {
		PlayerSettings settings = getMySettings();
		if (settings == null) {
			return badRequest();
		}
		return ok(toJson(settings));
	}

	@Transactional
	public static Result update() {
		PlayerSettings settings = getMySettings();
		if (settings == null) {
			return badRequest();
		}
		try {
			Object result = instance.update(settings);
			if (result != null) {
				return ok(toJson(result));
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return badRequest();
	}

	private static PlayerSettings getMySettings() {
		User user = Application.getLocalUser(session());
		if (user != null && user.getPlayer() != null && user.getPlayer().getSettings() != null) {
			return user.getPlayer().getSettings();
		}
		return null;
	}

}
