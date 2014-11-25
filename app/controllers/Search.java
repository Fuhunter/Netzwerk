/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * Network Controller is index for logged in Users
 */

package controllers;

import play.*;
import play.mvc.*;
import views.html.*;


public class Search extends Controller {

    public static Result index() {
        return ok(search.render(session().get("email")));
    }

    public static Result search() {
        return ok(search.render(session().get("email")));
    }
}