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
import models.Gruppenmitglieder;
import models.Users;


public class Groups extends Controller {

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
            Groupsmodel newGroup = new Groupsmodel();
            newGroup.setGruppenname(gruppenname);
            newGroup.setGruppenbeschreibung(gruppenbeschreibung);
            newGroup.setGruppentags(gruppentags);
            newGroup.save();
            Gruppenmitglieder newMember = new Gruppenmitglieder();
            newMember.setGroup(newGroup.getId());
        }
        catch (Exception e) {
            Logger.error("Error", e);
            return badRequest(groups.render(session().get("email"), true, "Datenbank Fehler"));
        }

        return ok(groups.render(session().get("email"), false, "Success"));
    }

    public static String getUserId(){
       return session().get("id");
    }

}