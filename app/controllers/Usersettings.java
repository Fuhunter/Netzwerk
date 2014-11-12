/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * Network Controller is index for logged in Users
 */

package controllers;

import play.*;
import play.mvc.*;
import views.html.*;

public class Usersettings extends Controller {

    /**
     * If authenticated render Indexpage
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result index() {
        return ok(settings.render(session().get("email")));
    }

    /**
     * Update User settings
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result update() {
        return ok(settings.render(session().get("email")));
    }
}
