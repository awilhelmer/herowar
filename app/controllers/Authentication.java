package controllers;

import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import com.ssachtleben.play.plugin.auth.Auth;
import com.ssachtleben.play.plugin.auth.Providers;
import com.ssachtleben.play.plugin.auth.providers.BaseOAuthProvider;
import com.ssachtleben.play.plugin.auth.providers.Facebook;
import com.ssachtleben.play.plugin.auth.providers.Google;

public class Authentication extends Controller {

	@SuppressWarnings("rawtypes")
	public static Result url(final String provider) {
		return redirect(((BaseOAuthProvider) Providers.get(provider)).authUrl());
	}

	@Transactional
	public static Result login(final String provider) {
		return Auth.login(ctx(), provider);
	}

	public static Result logout() {
		return Auth.logout(session());
	}

	public static Result close(final String provider) {
		if (Facebook.KEY.equals(provider)) {
			return ok(views.html.login.callback.facebook.render());
		} else if (Google.KEY.equals(provider)) {
			return ok(views.html.login.callback.google.render());
		}
		return badRequest();
	}	
}
