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


public class Group extends Controller {

    /**
     * If authenticated render Indexpage
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result index() {
        return ok(groups.render(session().get("email"), false, ""));
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
            return badRequest(groups.render(session().get("email"), true, "Gruppenname darf nicht leer sein"));
        }

        // Insert into Database
        try {
            Groups newGroup = new models.Groups();
            newGroup.setGruppenname(gruppenname);
            newGroup.setGruppenbeschreibung(gruppenbeschreibung);
            newGroup.setGruppentags(gruppentags);
            newGroup.save();
            Groupmembers newMember = new Groupmembers();
            newMember.setGroup(newGroup.getId());
        }
        catch (Exception e) {
            Logger.error("Error", e);
            return badRequest(groups.render(session().get("email"), true, "Datenbank Fehler"));
        }

        return ok(groups.render(session().get("email"), false, "Success"));
    }
}