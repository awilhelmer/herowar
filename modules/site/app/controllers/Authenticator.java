package controllers;

import common.models.entity.User;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * The Authenticator controller handles request for "/login" pattern.
 * 
 * @author Sebastian Sachtleben
 */
public class Authenticator extends Controller {

  public static Result login() {
    User user = Form.form(User.class).bindFromRequest().get();
    return redirect(routes.Application.index());
  }
}
