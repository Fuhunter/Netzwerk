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

        String[] birth = user.getBirth().split("\\.");

        return ok(settings.render(user.getEmail(), user.getVorname(), user.getNachname(), sex, user.getHomepage(), Integer.parseInt(birth[0]), birth[1], Integer.parseInt(birth[2]), user.getWohnort(), user.getBeschreibung(), false, ""));
    }

    /**
     * Update User settings
     * @return
     */
    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result update() {
        DynamicForm changes = new DynamicForm().bindFromRequest();

        String vorname = changes.get("vorname");
        String nachname = changes.get("nachname");
        String homepage = changes.get("homepage");
        String geschlecht = changes.get("geschlecht");
        String day = changes.get("tt");
        String month = changes.get("mm");
        String year = changes.get("yyyy");
        String wohnort = changes.get("wohnort");
        String beschreibung = changes.get("beschreibung");


        Users user = Users.findByEmail(session().get("email"));

        try {
            Boolean del = Boolean.valueOf(changes.get("delete"));

            if (!del) {
                user.delete();
                session().clear();
                return redirect(routes.Signup.loginPage());
            }
        } catch (Exception e) {

        }

        try {
            // Check for empty field
            if (vorname.isEmpty() == true || vorname == "") {
                return redirect(routes.Usersettings.index());

            } else if (nachname.isEmpty() == true || nachname == "") {
                return redirect(routes.Usersettings.index());
            }
        } catch (Exception e) {
            return changePassword(changes, user);
        }

        try {
            if (changes.get("apassword") != null || !changes.get("apassword").isEmpty()) {
                return changePassword(changes, user);
            }
        } catch (Exception e) {
            user.setBirth(day + "." + month + "." + year);
            user.setVorname(vorname);
            user.setNachname(nachname);
            user.setSex(geschlecht);
            user.setHomepage(homepage);
            user.setBeschreibung(beschreibung);
            user.setWohnort(wohnort);
            user.save();
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

        String[] birth = user.getBirth().split("\\.");

        if (BCrypt.checkpw(oldpw, user.getPwsafe())) {
            String hashpw = BCrypt.hashpw(newpw, BCrypt.gensalt(12));
            Boolean matched = BCrypt.checkpw(checkpw, hashpw);
            if (matched == true) {
                user.setPwsafe(hashpw);
                user.save();
            } else {
                return badRequest(settings.render(user.getEmail(), user.getVorname(), user.getNachname(), sex, user.getHomepage(), Integer.parseInt(birth[0]), birth[1], Integer.parseInt(birth[2]), user.getWohnort(), user.getBeschreibung(), true, "Passwörter stimmen nicht überein!"));
            }
        } else {
            return badRequest(settings.render(user.getEmail(), user.getVorname(), user.getNachname(), sex, user.getHomepage(), Integer.parseInt(birth[0]), birth[1], Integer.parseInt(birth[2]), user.getWohnort(), user.getBeschreibung(), true, "Falsches Passwort"));
        }

        return ok(settings.render(user.getEmail(), user.getVorname(), user.getNachname(), sex, user.getHomepage(), Integer.parseInt(birth[0]), birth[1], Integer.parseInt(birth[2]), user.getWohnort(), user.getBeschreibung(),false, "Success!"));

    }
}
