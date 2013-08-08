package com.herowar.controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.admin.events;
import views.html.admin.index;

public class Admin extends Controller {

	public static Result index() {
		return ok(index.render());
	}

	public static Result userAll() {
		return ok(index.render());
	}

	public static Result userShow(Long id) {
		return ok(index.render());
	}

	public static Result mapAll() {
		return ok(index.render());
	}

	public static Result mapNew() {
		return ok(index.render());
	}

	public static Result mapShow(Long id) {
		return ok(index.render());
	}

	public static Result objectAll() {
		return ok(index.render());
	}

	public static Result objectNew() {
		return ok(index.render());
	}

	public static Result objectShow(Long id) {
		return ok(index.render());
	}

	public static Result newsAll() {
		return ok(index.render());
	}

	public static Result newsNew() {
		return ok(index.render());
	}

	public static Result newsShow(Long id) {
		return ok(index.render());
	}

	public static Result events() {
		return ok(events.render());
	}
}
