package controllers;

import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Authenticator extends Controller {

  public static Result login() {
    User user = Form.form(User.class).bindFromRequest().get();
    return redirect(routes.Application.index());
  }
}
