/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * Network Controller is index for logged in Users
 */

package controllers;

import play.*;
import play.data.DynamicForm;
import play.db.ebean.Transactional;
import play.mvc.*;
import views.html.*;


public class Groups extends Controller {

    /**
     * If authenticated render Indexpage
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result index() {
        return ok(groups.render(session().get("email"), "bla"));
    }




    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result register(){
        DynamicForm newGroupForm = new DynamicForm().bindFromRequest();
        String gruppenname = newGroupForm.get("gruppenname");
        String gruppenbeschreibung = newGroupForm.get("gruppenbeschreibung");
        String gruppentags = newGroupForm.get("gruppentags");
        return ok(groups.render(session().get("email"), "Success"));
    }
}
