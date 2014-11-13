/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * Network Controller is index for logged in Users
 */

package controllers;

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

        String birth[] = user.getBirth().split(".");
        int ubirth[] = {Integer.parseInt(birth[0]), Integer.parseInt(birth[2])};

        return ok(settings.render(user.getEmail(), user.getVorname(), user.getNachname(), sex, user.getHomepage(), false, ""));
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
        return redirect(routes.Usersettings.index());
    }

    public static Result changePassword() {
        return redirect(routes.Usersettings.index());
    }
}
