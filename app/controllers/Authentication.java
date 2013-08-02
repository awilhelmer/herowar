package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import com.ssachtleben.play.plugin.auth.Auth;

public class Authentication extends Controller {

	public static Result login(final String provider) {
		return Auth.login(ctx(), provider);
	}

}
