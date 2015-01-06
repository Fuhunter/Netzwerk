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

public class Messages extends Controller {

    /**
     * If authenticated render Indexpage
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result index() {
        return ok();
    }
}
