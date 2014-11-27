/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * Network Controller is index for logged in Users
 */

package controllers;

import play.*;
import play.mvc.*;
import views.html.*;

import models.*;

import java.util.ArrayList;
import java.util.List;

public class Network extends Controller {

    /**
     * If authenticated render Indexpage
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result index() {
        return ok(network.render(session().get("email")));
    }

    @Security.Authenticated(Secured.class)
    public static Result statistics() {
        Integer users = Users.find.findRowCount();
        Integer groups = Groups.find.findRowCount();
        Integer online = Users.find.where().isNotNull("sessionid").findRowCount();
        Integer owngroups = Groupmembers.find.where().eq("user_id", session().get("userid")).findRowCount();

        return ok(statistics.render(session().get("email"), users, groups, online, owngroups));
    }

    @Security.Authenticated(Secured.class)
    public static Result showUser(Long id) {
        Users user = Users.findById(id);

        if (user == null) {
            return badRequest(userprofile.render(session().get("email"), null));
        }
        return ok(userprofile.render(session().get("email"), user));
    }

    @Security.Authenticated(Secured.class)
    public static Result showGroup(String gruppenname) {
        Groups group = Groups.findByGruppenname(gruppenname);
        List<Groupmembers> gmembers = Groupmembers.find.where().eq("group_id", group.getId()).findList();
        List<Users> gmemberss = new ArrayList<Users>();

        if (gmembers.size() != 0) {
            for (Groupmembers user : gmembers) {
                Users member = Users.findById(user.getId());
                if (member != null) {
                    gmemberss.add(member);
                }
            }
        }

        if(Groupmembers.find.where().eq("user_id", session().get("userid")).eq("group_id", group.getId()).findRowCount() == 0) {
            return badRequest(groupprofile.render(session().get("email"), group, false, gmemberss));
        }


        Groupmembers member = Groupmembers.findById(Long.parseLong(session().get("userid")), group.getId());
        if (member.getMember() == Long.parseLong(session().get("userid")) && member.getGroup() == group.getId()) {
            return ok(groupprofile.render(session().get("email"), group, true, gmemberss));
        }
        return badRequest(groupprofile.render(session().get("email"), group, false, gmemberss));

    }

    @Security.Authenticated(Secured.class)
    public static Result leaveGroup(String name) {

        Groups group = Groups.findByGruppenname(name);
        Groupmembers member = Groupmembers.findById(Long.parseLong(session().get("userid")), group.getId());
        member.delete();
        Integer leftmembers = Groupmembers.find.where().eq("group_id", group.getId()).findRowCount();
        if(leftmembers == 0){
            group.delete();
            return redirect(routes.Search.index());
        }
        return redirect(routes.Network.showGroup(name));
    }

    @Security.Authenticated(Secured.class)
    public static Result enterGroup(String name){

        Groups group = Groups.findByGruppenname(name);
        Groupmembers newMember = new models.Groupmembers();
        newMember.setMember(Long.parseLong(session().get("userid")));
        newMember.setGroup(group.getId());
        newMember.save();
        return redirect(routes.Network.showGroup(name));
    }
}
