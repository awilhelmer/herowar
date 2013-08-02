package service;

import play.Logger;
import play.mvc.Http.Context;

import com.ssachtleben.play.plugin.auth.annotations.Authenticates;
import com.ssachtleben.play.plugin.auth.models.GoogleAuthUser;

/**
 * Handles authentication for different providers.
 * 
 * @author Sebastian Sachtleben
 */
public class AuthService {
	private static final Logger.ALogger log = Logger.of(AuthService.class);

	@Authenticates(type = GoogleAuthUser.class)
	public static void handleGoogleLogin(final Context context, final GoogleAuthUser identity) {
		log.info("~~~ handleGoogleLogin() ~~~");
	}

}
