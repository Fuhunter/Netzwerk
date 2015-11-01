/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * Application Controller shows Indexpages
 */

package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    /**
     * Show indexpage
     * @return Elements for View
     */
    public static Result index() {
        return ok(index.render());
    }

    /**
     * show aboutpage
     * @return Elements for View
     */
    public static Result about() {
        return ok(about.render(null));
    }

    /**
     * show impressum
     * @return Elements for View
     */
    public static Result impressum() {
        return ok(impressum.render());
    }
}
