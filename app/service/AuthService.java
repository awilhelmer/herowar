package service;

import models.entity.User;

import org.apache.commons.lang.RandomStringUtils;
import org.codehaus.jackson.JsonNode;

import play.Logger;
import play.mvc.Http.Context;

import com.ssachtleben.play.plugin.auth.annotations.Authenticates;
import com.ssachtleben.play.plugin.auth.models.EmailPasswordAuthUser;
import com.ssachtleben.play.plugin.auth.models.FacebookAuthUser;
import com.ssachtleben.play.plugin.auth.models.GoogleAuthUser;
import com.ssachtleben.play.plugin.auth.models.UsernamePasswordAuthUser;
import com.ssachtleben.play.plugin.auth.providers.Facebook;
import com.ssachtleben.play.plugin.auth.providers.Google;
import com.ssachtleben.play.plugin.auth.providers.UsernamePassword;

import dao.UserDAO;

/**
 * Handles authentication for different providers.
 * 
 * @author Sebastian Sachtleben
 */
public class AuthService {
	private static final Logger.ALogger log = Logger.of(AuthService.class);

	/**
	 * Handles authentication via {@link Google} provider.
	 * 
	 * @param ctx
	 *          The {@link Context} to set.
	 * @param identity
	 *          The {@link GoogleAuthUser} to set.
	 * @return The user id as object.
	 */
	@Authenticates(provider = Google.KEY)
	public static Object handleGoogleLogin(final Context ctx, final GoogleAuthUser identity) {
		log.info(String.format("~~~ handleGoogleLogin() [ctx=%s, identity=%s] ~~~", ctx, identity));
		final JsonNode data = identity.data();
		final String email = data.get("email").getTextValue();
		final String username = data.get("given_name").getTextValue();
		final String password = RandomStringUtils.randomAlphabetic(10);
		log.info("Email: " + email);
		log.info("Username: " + username);
		log.info("Password: " + password);
		return handleOAuthLogin(ctx, email, username, password);
	}

	/**
	 * Handles authentication via {@link Facebook} provider.
	 * 
	 * @param ctx
	 *          The {@link Context} to set.
	 * @param identity
	 *          The {@link FacebookAuthUser} to set.
	 * @return The user id as object.
	 */
	@Authenticates(provider = Facebook.KEY)
	public static Object handleFacebookLogin(final Context ctx, final FacebookAuthUser identity) {
		log.info(String.format("~~~ handleFacebookLogin() [ctx=%s, identity=%s] ~~~", ctx, identity));
		final JsonNode data = identity.data();
		final String email = data.get("email").getTextValue();
		final String username = data.get("first_name").getTextValue();
		final String password = RandomStringUtils.randomAlphabetic(10);
		log.info("Email: " + email);
		log.info("Username: " + username);
		log.info("Password: " + password);
		return handleOAuthLogin(ctx, email, username, password);
	}

	/**
	 * Handles authentication via {@link UsernamePassword} provider.
	 * 
	 * @param ctx
	 *          The {@link Context} to set.
	 * @param identity
	 *          The {@link EmailPasswordAuthUser} to set.
	 * @return The user id as object.
	 */
	@Authenticates(provider = UsernamePassword.KEY)
	public static Object handleUsernameLogin(final Context ctx, final UsernamePasswordAuthUser identity) {
		log.info(String.format("~~~ handleUsernameLogin() [ctx=%s, identity=%s] ~~~", ctx, identity));
		log.info("Username: " + identity.id());
		log.info("Password: " + identity.clearPassword());
		User user = UserDAO.findByUsername(identity.id());
		// TODO: Check if password is valid for this user ...
		log.warn("Password compare not implemented yet...");
		return user != null ? user.getId() : null;
	}

	/**
	 * Checks if {@link User} exists with this email and returns the user id.
	 * <p>
	 * If no user found we create a new one.
	 * </p>
	 * 
	 * @param ctx
	 *          The {@link Context} to set.
	 * @param email
	 *          The email to set.
	 * @param username
	 *          The username to set.
	 * @param password
	 *          The password to set.
	 * @return The user id as object.
	 */
	public static Object handleOAuthLogin(final Context ctx, final String email, final String username, final String password) {
		User user = UserDAO.findByEmail(email);
		log.info(String.format("Found user: %s", user));
		if (user == null) {
			user = UserDAO.create(UserDAO.findUniqueUsername(username), password, email);
			log.info(String.format("Created new user: %s", user));
		}
		return user != null ? user.getId() : null;
	}
}
