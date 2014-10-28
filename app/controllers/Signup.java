package controllers;

import assets.BCrypt;
import com.avaje.ebean.Ebean;
import models.Users;
import play.*;
import play.data.*;
import play.data.Form.*;
import play.data.DynamicForm;
import play.mvc.*;
import views.html.*;

public class Signup extends Controller {

    public static Result index() {
        return ok(signup.render(null, null));
    }

    public static Result register() {
        DynamicForm newUserForm = new DynamicForm().bindFromRequest();
        String vorname = newUserForm.get("vorname");
        String nachname = newUserForm.get("nachname");
        String email = newUserForm.get("email");
        String geschlecht = newUserForm.get("geschlecht");
        String password = newUserForm.get("password");
        String password2 = newUserForm.get("rpassword");
        String hashpass = BCrypt.hashpw(password, BCrypt.gensalt(12));
        boolean matched = BCrypt.checkpw(password2, hashpass);
        String day = newUserForm.get("tt");
        String month = newUserForm.get("mm");
        String year = newUserForm.get("yyyy");

        if (matched != true) {
            return badRequest(signup.render(true, "Passwort nicht identisch!"));
        }

        try {
            Users newUser = new Users();
            newUser.birth = day + "." + month + "." + year;
            newUser.email = email;
            newUser.vorname = vorname;
            newUser.nachname = nachname;
            newUser.sex = geschlecht;
            newUser.pwsafe = hashpass;
            newUser.active = 0;
            newUser.blocked = 0;
            newUser.save();
        }
        catch (Exception e) {
            return badRequest(signup.render(true, "Datenbank Fehler"));
        }

        return ok(signup.render(false, "Success"));
    }

    public static Result login() {
        DynamicForm loginForm = new DynamicForm().bindFromRequest();
        String password = loginForm.get("password");
        String email = loginForm.get("email");

        Users user = Users.findByEmail(email);

        if (user == null) {
            return badRequest(login.render(true, "Email Adresse falsch!"));
        }
        else if (BCrypt.checkpw(password,user.pwsafe) == false) {
            return badRequest(login.render(true, "Falsches Passwort!"));
        }
        else {
            session().clear();
            session("email", email);
            session("userid", Long.toString(user.id));
            return redirect(routes.Network.index());
        }
    }

    public static Result loginPage() {
        return ok(login.render(false, null));
    }

    public static Result logout() {
        session().clear();
        return redirect(routes.Application.index());
    }
}