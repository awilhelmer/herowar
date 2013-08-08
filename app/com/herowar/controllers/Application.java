package com.herowar.controllers;

import info.BuildInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.lang.math.NumberUtils;

import play.mvc.Controller;
import play.mvc.Http.Session;
import play.mvc.Result;
import views.html.index;

import com.herowar.dao.UserDAO;
import com.herowar.models.entity.User;
import com.ssachtleben.play.plugin.auth.Auth;


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

	private final static Map<String, List<String>> jsFiles = new HashMap<String, List<String>>();

	static {
		// TODO hardcoded prefix - Analyse from folder...
		jsFiles.put("admin", new ArrayList<String>());
		jsFiles.put("site", new ArrayList<String>());
		jsFiles.put("game", new ArrayList<String>());
		jsFiles.put("editor", new ArrayList<String>());
		jsFiles.get("admin").add("javascripts/av" + BuildInfo.cacheNumber() + ".js");
		jsFiles.get("admin").add("javascripts/as" + BuildInfo.cacheNumber() + ".js");
		jsFiles.get("admin").add("javascripts/at" + BuildInfo.cacheNumber() + ".js");
		jsFiles.get("site").add("javascripts/sv" + BuildInfo.cacheNumber() + ".js");
		jsFiles.get("site").add("javascripts/ss" + BuildInfo.cacheNumber() + ".js");
		jsFiles.get("site").add("javascripts/st" + BuildInfo.cacheNumber() + ".js");
		jsFiles.get("game").add("javascripts/gv" + BuildInfo.cacheNumber() + ".js");
		jsFiles.get("game").add("javascripts/gs" + BuildInfo.cacheNumber() + ".js");
		jsFiles.get("game").add("javascripts/gt" + BuildInfo.cacheNumber() + ".js");
		jsFiles.get("editor").add("javascripts/ev" + BuildInfo.cacheNumber() + ".js");
		jsFiles.get("editor").add("javascripts/es" + BuildInfo.cacheNumber() + ".js");
		jsFiles.get("editor").add("javascripts/et" + BuildInfo.cacheNumber() + ".js");
	}

	public static User getLocalUser() {
		return getLocalUser(session());
	}

	public static User getLocalUser(final Session session) {
		final String userId = Auth.getLoggedIn(session());
		final User user = NumberUtils.isNumber(userId) ? UserDAO.instance().findUnique(Long.parseLong(userId)) : null;
		return user;
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
		return jsFiles.get("site");
	}

	public static List<String> getJsFilesAdmin() {
		return jsFiles.get("admin");
	}

	public static List<String> getJsFilesGame() {
		return jsFiles.get("game");
	}

	public static List<String> getJsFilesEditor() {
		return jsFiles.get("editor");
	}
}
