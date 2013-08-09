package com.herowar.controllers;

import static play.libs.Json.toJson;

import java.lang.reflect.InvocationTargetException;

import com.herowar.models.entity.User;
import com.herowar.models.entity.game.PlayerSettings;

import play.db.jpa.Transactional;
import play.mvc.Result;

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
