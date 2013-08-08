package com.herowar.dao;

import java.util.Collections;
import java.util.Date;

import play.Logger;
import play.db.jpa.JPA;

import com.herowar.controllers.Application;
import com.herowar.models.entity.User;
import com.ssachtleben.play.plugin.auth.models.Identity;


public class UserDAO extends BaseDAO<Long, User> {
	private UserDAO() {
		super(Long.class, User.class);
	}

	private static final UserDAO instance = new UserDAO();

	@Deprecated
	public static UserDAO getInstance() {
		return instance;
	}

	public static UserDAO instance() {
		return instance;
	}

	public static User findByEmail(String email) {
		return instance().getSingleByPropertyValue("email", email);
	}

	public static String findUniqueUsername(String username) {
		if (!existsUsername(username)) {
			return username;
		}
		int count = 0;
		while (existsUsername(username + Integer.toString(count))) {
			count++;
		}
		return username + Integer.toString(count);
	}

	public static boolean existsUsername(String username) {
		return instance().countSingleByPropertyValue("username", username) > 0;
	}

	public static User create(final Identity identity, final String email, final String username, final String password) {
		final User user = new User();
		user.setRoles(Collections.singletonList(SecurityRoleDAO.findByRoleName(Application.USER_ROLE)));
		user.setActive(true);
		user.setLastLogin(new Date());
		user.setEmail(email);
		user.setEmailValidated(false);
		user.setUsername(username);
		JPA.em().persist(user);
		user.setLinkedAccounts(Collections.singletonList(LinkedAccountDAO.create(identity, user)));
		Logger.info("Saved new user " + user.getUsername());
		return user;
	}

	public static void delete(User user) {
		JPA.em().remove(user);
	}

	public static Object findByUserEmail(String email) {
		return instance.getSingleByPropertyValue("email", email);
	}

	public static User findByUsername(final String username) {
		return instance.getSingleByPropertyValue("username", username);
	}

	public static void merge(User user, User user2) {
		user = instance.merge(user);
		user.setUsername(user2.getUsername());
		user.setEmail(user2.getEmail());
		user.setActive(user2.isActive());
		user.setNewsletter(user2.isNewsletter());
	}
}
