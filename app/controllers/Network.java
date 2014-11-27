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
        //Integer owngroups = Groupmembers.find.where().eq("user_id", session().get("userid")).findRowCount();

        return ok(statistics.render(session().get("email"), users, groups, online, 0));
    }

    @Security.Authenticated(Secured.class)
    public static Result showUser(Long id) {
        Users user = Users.findById(id);

        if (user == null) {
            return badRequest(userprofile.render(session().get("email"), null));
        }
        return ok(userprofile.render(session().get("email"), user));
    }

    @Security.Authenticated(Secured.class)
    public static Result showGroup(String name) {
        Groups group = Groups.findByGruppenname(name);

        if (Groupmembers.find.where().eq("user_id", session().get("userid")) == Groupmembers.find.where().eq("group_id", group.getId())) {

            return ok(groupprofile.render(session().get("email"), group, true));
        }
        return badRequest(groupprofile.render(session().get("email"), group, false));

    }
}
