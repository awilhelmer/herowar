package service;

import models.entity.LinkedAccount;
import models.entity.User;

import org.apache.commons.lang.RandomStringUtils;
import org.codehaus.jackson.JsonNode;

import play.Logger;
import play.mvc.Http.Context;

import com.ssachtleben.play.plugin.auth.annotations.Authenticates;
import com.ssachtleben.play.plugin.auth.models.FacebookAuthUser;
import com.ssachtleben.play.plugin.auth.models.GoogleAuthUser;
import com.ssachtleben.play.plugin.auth.models.Identity;
import com.ssachtleben.play.plugin.auth.models.PasswordUsernameAuthUser;
import com.ssachtleben.play.plugin.auth.providers.Facebook;
import com.ssachtleben.play.plugin.auth.providers.Google;
import com.ssachtleben.play.plugin.auth.providers.PasswordUsername;

import dao.LinkedAccountDAO;
import dao.UserDAO;

/**
 * Handles authentication for different providers.
 * 
 * @author Sebastian Sachtleben
 */
public class AuthService {
	private static final Logger.ALogger log = Logger.of(AuthService.class);

	@Authenticates(provider = Google.KEY)
	public static Object handleGoogleLogin(final Context ctx, final GoogleAuthUser identity) {
		log.info(String.format("~~~ handleGoogleLogin() [ctx=%s, identity=%s] ~~~", ctx, identity));
		final JsonNode data = identity.data();
		final String email = data.get("email").getTextValue();
		final String username = data.get("given_name").getTextValue();
		log.info("Email: " + email);
		log.info("Username: " + username);
		return handleLogin(identity, email, username, null);
	}

	@Authenticates(provider = Facebook.KEY)
	public static Object handleFacebookLogin(final Context ctx, final FacebookAuthUser identity) {
		log.info(String.format("~~~ handleFacebookLogin() [ctx=%s, identity=%s] ~~~", ctx, identity));
		final JsonNode data = identity.data();
		final String email = data.get("email").getTextValue();
		final String username = data.get("first_name").getTextValue();
		log.info("Email: " + email);
		log.info("Username: " + username);
		return handleLogin(identity, email, username, null);
	}

	@Authenticates(provider = PasswordUsername.KEY)
	public static Object handleUsernameLogin(final Context ctx, final PasswordUsernameAuthUser identity) {
		log.info(String.format("~~~ handleUsernameLogin() [ctx=%s, identity=%s] ~~~", ctx, identity));
		log.info("Username: " + identity.username());
		log.info("Password: " + identity.clearPassword());
		log.info("HashedPW: " + identity.id());
		LinkedAccount account = LinkedAccountDAO.findByUsername(PasswordUsername.KEY, identity.username());
		log.info("Account: " + account);
		log.info("Check PW: " + PasswordUsernameAuthUser.checkPassword(account.getProviderUserId(), identity.clearPassword()));
		return PasswordUsernameAuthUser.checkPassword(account.getProviderUserId(), identity.clearPassword()) ? account.getUser().getId() : null;
	}

	public static Object handleLogin(final Identity identity, final String email, final String username, final String password) {
		User user = null;
		LinkedAccount linkedAccount = LinkedAccountDAO.find(identity.provider(), identity.id());
		log.info(String.format("Found LinkedAccount: %s", linkedAccount));
		if (linkedAccount == null) {
			log.info(String.format("No linked account found for %s", identity));
			if (email != null) {
				// User comes from oauth, otherwise email is not set
				user = UserDAO.findByEmail(email);
				if (user == null) {
					user = UserDAO.create(identity, email, UserDAO.findUniqueUsername(username), RandomStringUtils.randomAlphanumeric(10));
					log.info(String.format("Created new user: %s", user));
				}
			} else {
				// User uses username / password for login
				user = UserDAO.findByUsername(identity.id());
				// TODO: Check if password is valid for this user ...
				log.warn("Password compare not implemented yet...");
			}
		} else {
			user = linkedAccount.getUser();
		}
		log.info(String.format("Found user: %s", user));
		return user != null ? user.getId() : null;
	}
}
