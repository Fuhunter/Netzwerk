/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * Global Application Configurations
 */

import play.*;
import play.mvc.*;
import play.mvc.Http.*;
import play.libs.F.*;

import static play.mvc.Results.*;

/**
 * Global Settings class
 */
public class Global extends GlobalSettings {

    /**
     * If an error occures, show error page
     * @param request request
     * @param t error
     * @return error page
     */
    public Promise<Result> onError(RequestHeader request, Throwable t) {
        return Promise.<Result>pure(internalServerError(views.html.errorPage.render(t)));
    }

    /**
     * Show Not Found Page if an URL should be opened, that not exists
     * @param request request
     * @return error page
     */
    public Promise<Result> onHandlerNotFound(RequestHeader request) {
        return Promise.<Result>pure(notFound(
                views.html.notFoundPage.render(request.uri())
        ));
    }

    /**
     * Show's error if BadRequest
     * @param request request
     * @param error error
     * @return errorpage
     */
    public Promise<Result> onBadRequest(RequestHeader request, String error) {
        return Promise.<Result>pure(badRequest("Don't try to hack the URI!"));
    }
}
