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
        DynamicForm newUser = new DynamicForm().bindFromRequest();
        String vorname = newUser.get("vorname");
        String nachname = newUser.get("nachname");
        String email = newUser.get("email");
        String geschlecht = newUser.get("geschlecht");
        String password = newUser.get("password");
        String password2 = newUser.get("rpassword");
        String hashpass = BCrypt.hashpw(password, BCrypt.gensalt(12));
        boolean matched = BCrypt.checkpw(password2, hashpass);

        if (matched != true) {
            return badRequest(signup.render("Passwort nicht identisch!"));
        }

        return ok(signup.render("Success"));
    }
}