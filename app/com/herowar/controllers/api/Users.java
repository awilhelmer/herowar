package com.herowar.controllers.api;

import static play.libs.Json.toJson;

import com.herowar.models.entity.User;

import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Result;

/**
 * The Users controller handle api requests for the User model.
 * 
 * @author Sebastian Sachtleben
 */
public class Users extends BaseAPI<Long, User> {

	private Users() {
		super(Long.class, User.class);
	}

	public static final Users instance = new Users();

	@Transactional
	public static Result list() {
		return instance.listAll();
	}

	@Transactional
	public static Result show(Long id) {
		return instance.showEntry(id);
	}

	@Transactional
	public static Result update(Long id) {
		User user = instance.merge(Form.form(User.class).bindFromRequest().get());
		return ok(toJson(user));
	}

	@Transactional
	public static Result delete(Long id) {
		return instance.deleteEntry(id);
	}

	@Transactional
	public static Result add() {
		return instance.addEntry();
	}

}
