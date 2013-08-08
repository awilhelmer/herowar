package com.herowar.controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class ShaderTest extends Controller {

	public static Result index() {
		return ok(views.html.game.shadertest.render());
	}
}
