/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * Network Controller is index for logged in Users
 */

package controllers;

import javafx.geometry.Pos;
import play.*;
import play.data.DynamicForm;
import play.db.ebean.Transactional;
import play.mvc.*;
import views.html.*;

import models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Network extends Controller {

    /**
     * If authenticated render Indexpage
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result index() {
        List<Friendship> ufriended = new ArrayList<>();
        List<Users> uufriended = new ArrayList<>();

        ufriended = Friendship.find.where().eq("user_id", session().get("userid")).findList();

        for (Friendship f : ufriended) {
            uufriended.add(Users.findById(f.getFriendid()));
        }

        List<List<Post>> postshelp = new ArrayList<>();
        for (Users u : uufriended) {

            List<Post> helplist = Post.find.where().eq("poster_id", u.getId()).findList();
            postshelp.add(helplist);
        }

        List<Post> myposts = Post.find.where().eq("poster_id", session().get("userid")).findList();
        postshelp.add(myposts);
        List<Post> posts = new ArrayList<>();
        for(List<Post> p : postshelp){
            for(Post i : p){
                posts.add(i);
            }
        }


        return ok(network.render(session().get("email"), false, "", posts));
    }

    /**
     * Show Endorado statistics
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result statistics() {
        Integer users = Users.find.findRowCount();
        Integer groups = Groups.find.findRowCount();
        Integer online = Users.find.where().isNotNull("sessionid").findRowCount();
        Integer owngroups = Groupmembers.find.where().eq("user_id", session().get("userid")).findRowCount();

        return ok(statistics.render(session().get("email"), users, groups, online, owngroups));
    }

    /**
     * Show user profile
     * @param id
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result showUser(Long id) {
        Users user = Users.findById(id);

        if (user == null) {
            return badRequest(userprofile.render(session().get("email"), null, null, null, null));
        }

        Integer check = Friendship.find.where().eq("friend_id", id).eq("user_id", session().get("userid")).findRowCount();
        Integer check1 = Friendship.find.where().eq("friend_id", session().get("userid")).eq("user_id", id).findRowCount();

        Boolean friends = false;
        Boolean ufriended = false;
        Boolean hfriended = false;

        if (check == 1) {
            ufriended = true;
        }

        if (check1 == 1) {
            hfriended = true;
        }

        if (check1 == 1 && check == 1) {
            friends = true;
        }

        return ok(userprofile.render(session().get("email"), user, ufriended, hfriended, friends));
    }

    /**
     * Show Groupprofile
     * @param gruppenname
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result showGroup(String gruppenname) {
        Groups group = Groups.findByGruppenname(gruppenname);
        List<Groupmembers> gmembers = Groupmembers.find.where().eq("group_id", group.getId()).findList();
        List<Users> gmemberss = new ArrayList<>();

        if (gmembers.size() != 0) {
            for (Groupmembers user : gmembers) {
                Users member = Users.findById(user.getMember());
                if (member != null) {
                    gmemberss.add(member);
                }
            }
        }

        if(Groupmembers.find.where().eq("user_id", session().get("userid")).eq("group_id", group.getId()).findRowCount() == 0) {
            return ok(groupprofile.render(session().get("email"), group, false, gmemberss));
        }


        Groupmembers member = Groupmembers.findById(Long.parseLong(session().get("userid")), group.getId());
        if (member.getMember() == Long.parseLong(session().get("userid")) && member.getGroup() == group.getId()) {
            return ok(groupprofile.render(session().get("email"), group, true, gmemberss));
        }
        return ok(groupprofile.render(session().get("email"), group, false, gmemberss));

    }

    /**
     * Leave Group and delete if last member
     * @param name
     * @return
     */
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

    /**
     * Join group
     * @param name
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result enterGroup(String name){

        Groups group = Groups.findByGruppenname(name);
        Groupmembers newMember = new models.Groupmembers();
        newMember.setMember(Long.parseLong(session().get("userid")));
        newMember.setGroup(group.getId());
        newMember.save();
        return redirect(routes.Network.showGroup(name));
    }

    /**
     * Creates a post
     * @return
     */
    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result posts(){

        DynamicForm newPostForm = new DynamicForm().bindFromRequest();
        String post = newPostForm.get("post");
        Date date = new Date();
        try {
            Post newPost = new Post();
            Users help_User = Users.findById(Long.parseLong(session().get("userid")));
            newPost.setPoster(Long.parseLong(session().get("userid")));
            newPost.setPoster_name(help_User.getVorname() + " " + help_User.getNachname());
            newPost.setText(post);
            newPost.setTimestamp(date);
            newPost.save();
        }
        catch (Exception e) {
            Logger.error("Error", e);
            return badRequest(network.render(session().get("email"), true, "Datenbank Fehler",null));
        }

        return redirect(routes.Network.index());

    }

    @Security.Authenticated(Secured.class)
    public static Result showpost(Long id){
        Post helppost = Post.findById(id);

        return ok(post.render(session().get("email"), false, "", helppost));
    }

    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result repost(Long id){
        Post helppost = Post.findById(id);
        DynamicForm newPostForm = new DynamicForm().bindFromRequest();
        String post = newPostForm.get("npost");
        Logger.debug("test " + post);
        helppost.setText(post);
        helppost.save();
        return redirect(routes.Network.index());
    }

    @Security.Authenticated(Secured.class)
    public static Result delpost(Long id){
        Post helppost = Post.findById(id);
        helppost.delete();
        return redirect(routes.Network.index());
    }
}
