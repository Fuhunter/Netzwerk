package controllers;

import play.*;
import play.mvc.*;
import views.html.*;

public class Network extends Controller {

    @Security.Authenticated(Secured.class)
    public static Result index() {
        return ok(network.render());
    }
}
