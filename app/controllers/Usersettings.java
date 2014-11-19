/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * Network Controller is index for logged in Users
 */

package controllers;

import assets.BCrypt;
import models.Users;
import play.*;
import play.data.DynamicForm;
import play.db.ebean.Transactional;
import play.mvc.*;
import views.html.*;

import java.util.Arrays;
import java.util.List;

public class Usersettings extends Controller {

    /**
     * If authenticated render Indexpage
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result index() {
        Users user = Users.findByEmail(session().get("email"));

        Boolean sex = false;

        if (user.getSex().equals("Männlich")) {
            sex = true;
        }

        String[] birth = user.getBirth().split(".");

        //int ubirth[] = {Integer.parseInt(birth[0]), Integer.parseInt(birth[2])};

        return ok(settings.render(user.getEmail(), user.getVorname(), user.getNachname(), sex, user.getHomepage(), birth, false, ""));
    }

    /**
     * Update User settings
     * @return
     */
    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result update() {
        DynamicForm changes = new DynamicForm().bindFromRequest();

        Users user = Users.findByEmail(session().get("email"));

        if (changes.get("apassword") != null || !changes.get("apassword").isEmpty()) {
            return changePassword(changes, user);
        }

        return redirect(routes.Usersettings.index());
    }

    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result changePassword(DynamicForm changes, Users user) {
        String oldpw = changes.get("apasswort");
        String newpw = changes.get("npasswort");
        String checkpw = changes.get("rpasswort");

        Boolean sex = false;

        if (user.getSex().equals("Männlich")) {
            sex = true;
        }

        String[] birth = user.getBirth().split(".");
        //int ubirth[] = {Integer.parseInt(birth[0]), Integer.parseInt(birth[2])};

        if (BCrypt.checkpw(oldpw, user.getPwsafe())) {
            if (newpw == checkpw) {
                String hashpw = BCrypt.hashpw(newpw, BCrypt.gensalt(12));
                user.setPwsafe(hashpw);
                user.save();
            } else {
                return badRequest(settings.render(user.getEmail(), user.getVorname(), user.getNachname(), sex, user.getHomepage(), birth, true, "Passwörter stimmen nicht überein!"));
            }
        } else {
            return badRequest(settings.render(user.getEmail(), user.getVorname(), user.getNachname(), sex, user.getHomepage(), birth, true, "Falsches Passwort"));
        }

        return ok(settings.render(user.getEmail(), user.getVorname(), user.getNachname(), sex, user.getHomepage(), birth, false, "Success!"));
    }
}
