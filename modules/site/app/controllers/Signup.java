package controllers;

import models.common.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.signup;

/**
 * The Signup controller handles request for "/signup" pattern.
 * 
 * @author Sebastian Sachtleben
 */
public class Signup extends Controller {

  public static Result index() {
    return ok(signup.render());
  }
  
  public static Result send() {
    User user = Form.form(User.class).bindFromRequest().get();
    user.save();
    return redirect(routes.Application.index());
  }
  
}
