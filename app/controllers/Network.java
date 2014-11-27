/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * Network Controller is index for logged in Users
 */

package controllers;

import play.*;
import play.mvc.*;
import views.html.*;

import models.*;

import java.util.List;

public class Network extends Controller {

    /**
     * If authenticated render Indexpage
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result index() {
        return ok(network.render(session().get("email")));
    }

    @Security.Authenticated(Secured.class)
    public static Result statistics() {
        Integer users = Users.find.findRowCount();
        Integer groups = Groups.find.findRowCount();
        Integer online = Users.find.where().isNotNull("sessionid").findRowCount();
        Integer owngroups = Groupmembers.find.where().eq("user_id", session().get("userid")).findRowCount();

        return ok(statistics.render(session().get("email"), users, groups, online, owngroups));
    }

    @Security.Authenticated(Secured.class)
    public static Result showUser(Long id) {
        Users user = Users.findById(id);

        if (user == null) {
            return badRequest(userprofile.render(session().get("email"), null));
        }
        return ok(userprofile.render(session().get("email"), user));
    }
}
