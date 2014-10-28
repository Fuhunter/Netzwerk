package controllers;

import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;
import views.html.*;

public class Signup extends Controller {

    public static Result index() {
        return ok(signup.render(null));
    }

    public static Result register() {
        DynamicForm users = Form.form().bindFromRequest();
        Logger.info(users.get("vorname"));
        return ok("hallo " + users.get("vorname"));
    }
}