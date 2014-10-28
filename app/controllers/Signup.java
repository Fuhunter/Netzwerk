package controllers;

import assets.BCrypt;
import play.*;
import play.data.DynamicForm;
import play.mvc.*;
import views.html.*;

public class Signup extends Controller {

    public static Result index() {
        return ok(signup.render(null));
    }

    public static Result register() {
        DynamicForm users = new DynamicForm().bindFromRequest();
        String  originalPassword = users.get("password");
        String hashpass = BCrypt.hashpw(originalPassword, BCrypt.gensalt(12));
        boolean matched = BCrypt.checkpw(originalPassword, hashpass);

        return ok(signup.render("Success"));
    }
}