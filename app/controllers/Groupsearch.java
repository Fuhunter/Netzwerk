/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * Network Controller is index for logged in Users
 */

package controllers;

import models.Groupmembers;
import models.Groups;
import play.*;
import play.data.DynamicForm;
import play.db.ebean.Transactional;
import play.mvc.*;
import views.html.*;

import java.util.List;

public class Groupsearch extends Controller {

    public static Result index() {
        return ok(search.render(session().get("email"), "", null, false, ""));
    }

    public static Result search(){

        List<Groups> result = null;

        String group = "%" +"group" + "%";
        result = Groups.find.where().like("group", group).findList();
        return ok(groupsearch.render(session().get("email"), result, false, "Ergebnisse weiter unten"));
    }
}