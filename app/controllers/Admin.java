package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.admin.index;

public class Admin extends Controller {

  public static Result index() {
    return ok(index.render());
  }
  
  public static Result usersAll() {
    return ok(index.render());
  }
  
}
