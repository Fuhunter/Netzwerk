/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * Friends Controller for selecting friends (make friends, unfriend)
 */

package controllers;

import models.*;
import play.*;
import play.db.ebean.Transactional;
import play.mvc.*;
import views.html.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Freundschaft extends Controller {

    /**
     * Show friendships
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result index() {

        List<Friendship> ufriended = new ArrayList<>();
        List<Friendship> hfriended = new ArrayList<>();
        List<Friendship> friends = new ArrayList<>();
        List<Users> uufriended = new ArrayList<>();
        List<Users> hhfriended = new ArrayList<>();
        List<Users> friended = new ArrayList<>();

        ufriended = Friendship.find.where().eq("user_id", session().get("userid")).findList();
        hfriended = Friendship.find.where().eq("friend_id", session().get("userid")).findList();

        for (Friendship f : ufriended) {
            for (Friendship h: hfriended) {
                if (h.getFriendid() == f.getUserid()) {
                    friends.add(f);
                }
            }

            uufriended.add(Users.findById(f.getFriendid()));
        }

        for (Friendship h : hfriended) {
            hhfriended.add(Users.findById(h.getUserid()));
        }

        for (Friendship f : friends) {
            friended.add(Users.findById(f.getFriendid()));
        }

        return ok(friendship.render(session().get("email"), uufriended, hhfriended, friended, false, ""));
    }

    /**
     * add new friendship
     * @param id
     * @return
     */
    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result update(Long id) {
        // Check if friend exists
        Users checkId = Users.findById(id);
        if (checkId == null) {
            return badRequest(friendship.render(session().get("email"), null, null, null, true, "Diese ID existiert nicht"));
        }

        Date date = new Date();

        // Insert into Database
        try {
            Friendship newFriendship = new models.Friendship();
            newFriendship.setUserid(Long.parseLong(session().get("userid")));
            newFriendship.setFriendid(id);
            newFriendship.setTimestamp(date);
            newFriendship.save();
        } catch (Exception e) {
            Logger.error("Error", e);
            return badRequest(friendship.render(session().get("email"), null, null, null, true, "Datenbankfehler"));
        }

        return redirect(routes.Freundschaft.index());
    }

    /**
     * unfriend
     * @param id
     * @return
     */
    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result unfriend(Long id) {
        Users checkId = Users.findById(id);
        if (checkId == null) {
            return badRequest(friendship.render(session().get("email"), null, null, null, true, "Diese ID existiert nicht"));
        }

        Friendship friend = Friendship.find.where().eq("friend_id", id).eq("user_id", session().get("userid")).findUnique();

        friend.delete();

        return redirect(routes.Freundschaft.index());
    }

}