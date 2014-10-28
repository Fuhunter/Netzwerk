package controllers;

import assets.BCrypt;
import models.Users;
import play.*;
import play.data.DynamicForm;
import play.mvc.*;
import views.html.*;

public class Signup extends Controller {

    public static Result index() {
        return ok(signup.render(null, null));
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
        String day = newUser.get("tt");
        String month = newUser.get("mm");
        String year = newUser.get("yyyy");

        if (matched != true) {
            return badRequest(signup.render(true ,"Passwort nicht identisch!"));
        }

        Users nUser = new Users();
        nUser.birth = day + "." + month + "." + year;
        nUser.email = email;
        nUser.vorname = vorname;
        nUser.nachname = nachname;
        nUser.sex = geschlecht;
        nUser.pwsafe = hashpass;
        nUser.active = 0;
        nUser.blocked = 0;
        nUser.save();
        return ok(signup.render(false, "Success"));
    }

    public static Result login() {
        return ok();
    }
}