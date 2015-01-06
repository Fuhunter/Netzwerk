/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * Network Controller is index for logged in Users
 */

package controllers;

import play.mvc.*;
import views.html.*;
import models.Message;

import java.util.ArrayList;
import java.util.List;

public class Messages extends Controller {

    /**
     * If authenticated render Indexpage
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result index() {

        List<Message> messages = new ArrayList<>();
        List<String> betreff = new ArrayList<>();

        //messages = Message.find.where().eq("eid", session().get("userid")).findList();

        return ok(message.render(session().get("email")));
    }
}
