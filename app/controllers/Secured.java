package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.*;

import models.*;

import java.util.Date;

public class Secured extends Security.Authenticator {

    @Override
    public String getUsername(Context ctx) {
        // check if User is logged in
        if (ctx.session().get("email") == null) {
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
                return null;
            }
        }

        // update session tick
        String newTick = Long.toString(new Date().getTime());
        ctx.session().put("sessionTime", newTick);
        return ctx.session().get("email");
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.Signup.loginPage());
    }
}