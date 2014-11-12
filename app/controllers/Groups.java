/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * Network Controller is index for logged in Users
 */

package controllers;

import play.*;
import play.data.*;
import play.data.Form.*;
import com.avaje.ebean.Ebean;
import play.data.DynamicForm;
import play.db.ebean.Transactional;
import play.mvc.*;
import views.html.*;
import models.Groupsmodel;


public class Groups extends Controller {

    /**
     * If authenticated render Indexpage
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result index() {
        return ok(groups.render(session().get("email"), "bla"));
    }


    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result register() {
        DynamicForm newGroupForm = new DynamicForm().bindFromRequest();
        String gruppenname = newGroupForm.get("gruppenname");
        String gruppenbeschreibung = newGroupForm.get("gruppenbeschreibung");
        String gruppentags = newGroupForm.get("gruppentags");

        // Check for empty field
        if (gruppenname.isEmpty() == true || gruppenname == "") {
            return badRequest(groups.render(session().get("email"), "Gruppenname darf nicht leer sein"));
        }

        return ok(groups.render(session().get("email"), "Success"));
        // Insert into Database
        try {
            Groupsmodel newGroup = new Groupsmodel();
            newGroup.setGruppenname(gruppenname);
            newGroup.setGruppenbeschreibung(gruppenbeschreibung);
            newGroup.setGruppentags(gruppentags);
            newGroup.save();
        }
        catch (Exception e) {
            Logger.error("Error", e);
            return badRequest(groups.render(true, "Datenbank Fehler"));
        }

        return ok(groups.render(false, "Success"));
    }
}