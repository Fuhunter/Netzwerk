/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * Secured check Authorization for each Page
 */

package controllers;

import play.*;
import play.db.ebean.Transactional;
import play.mvc.*;
import play.mvc.Http.*;

import models.*;

import java.util.Date;

public class Secured extends Security.Authenticator {

    /**
     * Checks User Permissions and for SessionTimeout
     * @param ctx Context
     * @return Elements for View
     */
    @Override
    @Transactional
    public String getUsername(Context ctx) {
        // check if User is logged in
        if (ctx.session().get("email") == null || ctx.session().get("email").equals("") || ctx.session().get("id") == null) {
            ctx.session().clear();
            return null;
        }

        Users user = Users.findByEmail(ctx.session().get("email"));

        String sessionid = ctx.session().get("id");

        if (user.getBlocked() == 1 || user.getActive() == 0) {
            user.setSessionId(null);
            user.save();
            return null;
        }

        // DAFUQ??? WIESO klappt das?!
        if (!user.getSessionId().equals(sessionid)) {
            Logger.debug(user.getSessionId());
            Logger.debug(sessionid);
            Logger.error("2");
            user.setSessionId(null);
            user.save();
            return null;
        }

        // check for old Tick
        String prevTick = ctx.session().get("sessionTime");
        if (prevTick != null && !prevTick.equals("")) {
            long prevT = Long.valueOf(prevTick);
            long currentTime = new Date().getTime();
            long timeout = Long.valueOf(Play.application().configuration().getString("sessionTimeout")) * 1000 * 60;
            if ((currentTime - prevT) > timeout) {
                // session expired
                ctx.session().clear();
                user.setSessionId(null);
                user.save();
                return null;
            }
        }

        // update session tick
        String newTick = Long.toString(new Date().getTime());
        ctx.session().put("sessionTime", newTick);
        return ctx.session().get("email");
    }

    /**
     * Redirects to LoginPage if User is not authorized
     * @param ctx Context
     * @return Elements for View
     */
    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.Signup.loginPage());
    }
}