/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * Search Controller for searching Groups/Users
 */

package controllers;

import com.avaje.ebean.ExpressionList;
import models.Users;
import models.Groups;
import play.*;
import play.data.DynamicForm;
import play.db.ebean.Model;
import play.mvc.*;
import views.html.*;
import models.*;

import java.beans.Expression;
import java.util.List;

public class Search extends Controller {

    public static Result index() {
        return ok(search.render(session().get("email"), "", null, null, false, ""));
    }

    public static Result search() {
        DynamicForm sform = new DynamicForm().bindFromRequest();

        String checker = sform.get("check");

        List<Users> result = null;
        List<Groups> resultg = null;

        try {
            if (checker.equals("echeck")) {
                String email = "%" + sform.get("email") + "%";
                result = Users.find.where().like("email", email).findList();
                return ok(search.render(session().get("email"), checker, result, null, false, "Ergebnisse weiter unten"));
            } else if (checker.equals("vcheck")) {
                String vname = "%" + sform.get("vname") + "%";
                result = Users.find.where().like("vorname", vname).findList();
                return ok(search.render(session().get("email"), checker, result, null, false, "Ergebnisse weiter unten"));
            } else if (checker.equals("ncheck")) {
                String nname = "%" + sform.get("nname") + "%";
                result = Users.find.where().like("nachname", nname).findList();
                return ok(search.render(session().get("email"), checker, result, null, false, "Ergebnisse weiter unten"));
            }else if (checker.equals("gcheck")) {
                String gname = "%" + sform.get("gname") + "%";
                resultg = Groups.find.where().like("gruppenname", gname).findList();
                return ok(search.render(session().get("email"), checker, null, resultg, false, "Ergebnisse weiter unten"));
            }
        } catch (Exception e) {
            return badRequest(search.render(session().get("email"), "", null, null, true, "Du musst einen Suchparameter auswählen!"));
        }

        return null;
    }
}