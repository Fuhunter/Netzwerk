/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * Search Controller for searching Groups/Users
 */

package controllers;

import play.*;
import play.data.DynamicForm;
import play.db.ebean.Model;
import play.mvc.*;
import views.html.*;
import models.*;


public class Search extends Controller {

    public static Result index() {
        return ok(search.render(session().get("email"), false, ""));
    }

    public static Result search() {
        DynamicForm sform = new DynamicForm().bindFromRequest();

        return ok(search.render(session().get("email"), false, ""));
    }

    public static Result groupsearch() {
      return ok(search.render(session().get("email"), false, ""));
    }
}