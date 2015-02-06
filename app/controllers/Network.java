/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * Network Controller is index for logged in Users
 */

package controllers;

import javafx.geometry.Pos;
import play.*;
import play.Logger;
import play.api.libs.Collections;
import play.data.DynamicForm;
import play.db.ebean.Transactional;
import play.mvc.*;
import tyrex.util.ArraySet;
import views.html.*;

import models.*;

import java.util.*;
import java.util.logging.*;

public class Network extends Controller {

    /**
     * If authenticated render Indexpage
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result index() {

        List<Friendship> ufriended = new ArrayList<>();
        List<Friendship> hfriended = new ArrayList<>();
        List<Friendship> friends = new ArrayList<>();
        List<Users> uufriended = new ArrayList<>();
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

        for (Friendship f : friends) {
            friended.add(Users.findById(f.getFriendid()));
        }

        List<List<Post>> postshelp = new ArrayList<>();

        for (Users u : friended) {
            List<Post> helplist = Post.find.where().eq("poster_id", u.getId()).orderBy("timestamp").findList();
            postshelp.add(helplist);
        }

        List<Post> myposts = Post.find.where().eq("poster_id", session().get("userid")).orderBy("timestamp").findList();
        postshelp.add(myposts);
        for (Users u : uufriended){
            List<Post> helplist = Post.find.where().eq("poster_id",u.getId()).eq("public_post",(long) 1).orderBy("timestamp").findList();

            for (Post p : helplist) {
                if (postshelp.contains(p)) {
                    postshelp.add(helplist);
                }
            }
        }

        List<Post> posts = new ArrayList<>();
        for(List<Post> p : postshelp){
            for(Post i : p){
                posts.add(i);
            }
        }

        java.util.Collections.reverse(posts);

        Logger.debug("Matrix", cosine_amount());

        
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
            return badRequest(userprofile.render(session().get("email"), null, null, null, null,null));
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

        List<List<Post>> helpposts = new ArrayList<>();
        if (friends || Long.parseLong(session().get("userid")) == user.getId()){
            List<Post> help = Post.find.where().eq("poster_id", id).eq("public_post", 0).orderBy("timestamp").findList();
            helpposts.add(help);
        }

        List<Post> help = Post.find.where().eq("poster_id", id).eq("public_post", 1).orderBy("timestamp").findList();
        helpposts.add(help);
        List<Post> posts = new ArrayList<>();
        for(List<Post> p : helpposts){
            for(Post i : p){
                posts.add(i);
            }
        }

        java.util.Collections.reverse(posts);

        return ok(userprofile.render(session().get("email"), user, ufriended, hfriended, friends, posts));
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
        String publicpost = newPostForm.get("public");
        Logger.debug(publicpost);
        try {
            Post newPost = new Post();
            Users help_User = Users.findById(Long.parseLong(session().get("userid")));
            newPost.setPoster(Long.parseLong(session().get("userid")));
            newPost.setPoster_name(help_User.getVorname() + " " + help_User.getNachname());
            newPost.setText(post);
            newPost.setTimestamp(date);
            if (publicpost == null){
                newPost.setPublic_post((long) 0);
            }else {
                newPost.setPublic_post((long) 1);
            }
            newPost.save();
        }
        catch (Exception e) {
            Logger.error("Error", e);
            return badRequest(network.render(session().get("email"), true, "Datenbank Fehler",null));
        }

        return redirect(routes.Network.index());

    }

    /**
     * Show Post
     * @param id
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result showpost(Long id) {
        Post helppost = Post.findById(id);

        return ok(post.render(session().get("email"), false, "", helppost));
    }

    /**
     * Edit your Post
     * @param id
     * @return
     */
    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result repost(Long id) {
        Post helppost = Post.findById(id);
        DynamicForm newPostForm = new DynamicForm().bindFromRequest();
        String post = newPostForm.get("npost");
        Logger.debug("test " + post);
        helppost.setText(post);
        helppost.save();
        return redirect(routes.Network.index());
    }

    /**
     * Delete your Post
     * @param id
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result delpost(Long id) {
        Post helppost = Post.findById(id);
        helppost.delete();
        return redirect(routes.Network.index());
    }

    /**
     * Show friends with network analysis
     * @param id
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result netfriends(Long id) {
        List<Friendship> friend = new ArrayList<>();
        List<Friendship> myfriends = new ArrayList<>();
        List<Groupmembers> mygroups = new ArrayList<>();
        List<Friendship> gfriends = new ArrayList<>();
        List<Users> nonfriends = new ArrayList<>();
        List<Users> nongfriends = new ArrayList<>();
        List<Users> suggestions = new ArrayList<>();

        friend = Friendship.find.where().eq("friend_id", session().get("userid")).findList(); // friended with me
        myfriends = Friendship.find.where().eq("user_id", session().get("userid")).findList(); // friended with them
        mygroups = Groupmembers.find.where().eq("user_id", session().get("userid")).findList(); // mygroups

        // Check for double sided friendship
        if (!myfriends.isEmpty()) {
            for (Friendship f : friend) {
                for (Friendship ff : myfriends) {
                    if (f.getUserid() == ff.getFriendid()) {
                        friend.remove(f);
                        myfriends.remove(f);
                    }
                }
            }
        }

        // Get Users
        if (!friend.isEmpty()) {
            for (Friendship f: friend) {
                Users u = Users.findById(f.getUserid());

                if (u != null) {
                    nonfriends.add(u);
                }
            }
        }

        // Get suggestions from users i share a group with
        for (Groupmembers g: mygroups) {
            List<Groupmembers> gm = new ArrayList<>();

            gm = Groupmembers.find.where().eq("group_id", g.getGroup()).not(com.avaje.ebean.Expr.eq("user_id", session().get("userid"))).findList();

            for (Groupmembers gf: gm) {
                Friendship f = Friendship.find.where().eq("user_id", gf.getMember()).eq("friend_id", session().get("userid")).findUnique();

                if (f != null) {
                    gfriends.add(f);
                }
            }
        }

        if (!gfriends.isEmpty()) {
            for (Friendship gf: gfriends) {
                Users u = Users.findById(gf.getUserid());

                if (u != null) {
                    nongfriends.add(u);
                }
            }
        }

        // Remove double entry
        if (!nonfriends.isEmpty() && !nongfriends.isEmpty()) {
            for (Users u : nonfriends) {
                if (nongfriends.contains(u)) {
                    nongfriends.remove(u);
                }
            }
        }

        suggestions.addAll(nonfriends);
        suggestions.addAll(nongfriends);

        return ok(netfriends.render(session().get("email"), suggestions));
    }

    /**
     * Creates the user-word-matrix for content analysis
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static List<List<Map<String, Integer>>> creat_word_matrix()
    {
        List<Users> user = new ArrayList<>();
        List<Post> posts = new ArrayList<>();
        List<List<Map<String, Integer>>> wordmatrix = new ArrayList<>();
        String regexSpecialChars = "[^\\w\\s]";

        user = Users.find.all();
        Users huser = user.get(user.size() - 1);
        Long index = huser.getId();
        for(int i = 1; i < index+1; i++) {   //Initialization of the word matrix
            wordmatrix.add(new ArrayList<>());
        }

        for (Users u : user) {
            posts = Post.find.where().eq("poster_id", u.getId()).findList();
            if (!posts.isEmpty()){
                Map<String, Integer> words = new HashMap<String, Integer>();
                List<Map<String, Integer>> lwords = new ArrayList<>();
                for( Post p : posts){
                    String help = p.getPost().replaceAll(regexSpecialChars, "");
                    String[] singlewords = help.split(" ");
                    for(String s: singlewords){
                        s = s.toUpperCase();
                        if(words.containsKey(s)){
                            Integer value = words.get(s);
                            words.put(s, value + 1); //increase the number of a word
                        }
                        else{
                            words.put(s,1);// enter words of the user in the map of this user
                        }
                    }
                }
                lwords.add(words);
                Long enter = u.getId() - 1;
                wordmatrix.set(enter.intValue(),lwords);//create the word matrix
            }
        }


        return wordmatrix;
    }
    /**
     * Calculates the TF-IDF weighting for content analysis
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static List<List<Map<String, Float>>> tf_idf(){

        List<List<Map<String, Integer>>> userwordmatrix = creat_word_matrix();
        List<String> keywords = new ArrayList<>();
        Map<String, Integer> wordmatrix = new HashMap<String, Integer>();
        Integer activeuser = 0;
        Integer userid = 0;
        Float userwords = (float) 0.0;
        List<List<Float>> maxwords = new ArrayList<>();
        List<Users> user = new ArrayList<>();
        List<List<Map<String, Float>>> tf_idf_result = new ArrayList<>();

        user = Users.find.all();
        Users huser = user.get(user.size() - 1);
        Long index = huser.getId();
        /*Initialization of the TF IDF matrix*/
        for(int i = 1; i < index+1; i++){
            maxwords.add(new ArrayList<>());
            tf_idf_result.add(new ArrayList<>());
        }

        /*create a matrix which contains by how many users a special word is used*/
        for(List<Map<String, Integer>> lwords : userwordmatrix){
            for(Map<String, Integer> words :lwords){
                Set<String> swords = words.keySet();
                for(String s : swords){
                    keywords.add(s);
                    if (wordmatrix.containsKey(s)){
                        Integer value = wordmatrix.get(s);
                        wordmatrix.put(s, value + 1);
                    }else{
                        wordmatrix.put(s, 1);
                    }
                }
            }
        }

        /*create a matrix which contains the maximum amount of one word by one user*/
        for (List<Map<String, Integer>> lwords : userwordmatrix){
            if(!lwords.isEmpty()){
                activeuser = activeuser + 1; //count the number of users in the network
                for(Map<String, Integer> words :lwords){
                    for (String s : keywords){
                        if(words.containsKey(s)){
                            Float helpnumber = Float.parseFloat(String.valueOf(words.get(s)));
                            if (helpnumber > userwords){
                                userwords = helpnumber;
                            }
                        }
                    }
                }
                List<Float> helplist = new ArrayList<>();
                helplist.add(userwords);
                userwords = (float) 0.0;
                maxwords.set(userid, helplist);
            }
            userid = userid + 1;
        }

        userid = 0;
        /*create the TF IDF matrix*/
        for (List<Map<String, Integer>> lwords : userwordmatrix){
            if (!lwords.isEmpty()) {
                Map<String, Float> helpmap = new HashMap<>();
                for (Map<String, Integer> words : lwords) {
                    for (String s : keywords) {
                        s = s.toUpperCase();
                        if (words.containsKey(s)) {
                            List<Map<String, Float>> helplist = new ArrayList<>();
                            Float helpnumber = helpmap.put(s, helpmap.put(s, (float) ((words.get(s) / (maxwords.get(userid).get(0))) * (Math.log(activeuser / wordmatrix.get(s))))));
                            helpmap.put(s, helpnumber);
                            helplist.add(helpmap);
                            tf_idf_result.set(userid, helplist);
                        }

                    }
                }
            }
            userid = userid + 1;
        }
        return tf_idf_result;
    }

    /**
     * Calculates the cosine amount  for content analysis
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result cosine_amount() {

        List<List<Map<String, Float>>> tf_idf_matrix = tf_idf();
        List<List<Float>> cosine_amount_matrix = new ArrayList<>();
        List<Users> user = new ArrayList<>();
        Integer counter1 = 0;
        Integer counter2 = 0;
        Float cosine_amount_help1 = (float) 0.0;
        Float cosine_amount_help2 = (float) 0.0;
        Float cosine_amount_help3 = (float) 0.0;
        Float cosine_amount = (float) 0.0;
        Set<String> all_words = new ArraySet();

        user = Users.find.all();
        Users huser = user.get(user.size() - 1);
        Long index = huser.getId();
        /*Initialization of the TF IDF matrix*/
        for(int i = 1; i < index+1; i++){
            cosine_amount_matrix.add(new ArrayList<>());
        }

        for(List<Map<String, Float>> helplist : tf_idf_matrix) {
            for (Map<String, Float> helpmap : helplist) {
                Set<String> helpset = helpmap.keySet();
                for (String s : helpset){
                    all_words.add(s);
                }
            }
        }

        /*the try of the realization of the cosine amount formula*/
        for(List<Map<String, Float>> helplist : tf_idf_matrix){
            for (Map<String, Float> helpmap : helplist){
                for (int i = 1; i < tf_idf_matrix.size(); i++){
                    for (String helpstring : all_words){
                        if (helplist.get(0).containsKey(helpstring)){
                            cosine_amount_help1 = (float) (helplist.get(0).get(helpstring) * tf_idf_matrix.get(counter2).get(0).get(helpstring)) + cosine_amount_help1;
                            Logger.debug("cosine_amount1 " + cosine_amount_help1);
                            cosine_amount_help2 = (float) (helplist.get(0).get(helpstring) * helplist.get(0).get(helpstring)) + cosine_amount_help2;
                            Logger.debug("cosine_amount2 " + cosine_amount_help2);
                        }
                    }


                    for (String s : all_words){
                        if(tf_idf_matrix.get(counter2).get(0).containsKey(s)) {
                            cosine_amount_help3 = (float) (tf_idf_matrix.get(counter2).get(0).get(s) * tf_idf_matrix.get(counter2).get(0).get(s)) + cosine_amount_help3;
                            Logger.debug("cosine_amount3 " + cosine_amount_help3);
                        }
                    }

                    if (cosine_amount_help2 == 0.0 || cosine_amount_help3 == 0.0) {
                        cosine_amount = (float) 0.0;
                    } else {
                        cosine_amount = (float) (cosine_amount_help1 / (Math.sqrt(cosine_amount_help2) * Math.sqrt(cosine_amount_help3)));
                        Logger.debug(String.valueOf(cosine_amount));
                    }

                    cosine_amount_matrix.get(counter2).add(counter1, (float) cosine_amount);
                    counter1 += 1;
                    cosine_amount_help1 = (float) 0.0;
                    cosine_amount_help2 = (float) 0.0;
                    cosine_amount_help3 = (float) 0.0;
                }
            }
            counter1 = 0;
            counter2 += 1;
        }
        Logger.debug("Cosinus Matrix" + cosine_amount_matrix);


        return null;
    }

    /**
     * Get Friends with content analysis
     * @param id
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result contentfriends(Long id)
    {
        return ok(confriends.render(session().get("email"), null));
    }


    /**
     * create vote
     *
     * @param id
     * @return
     */
    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result createvote(Long id) {

        Date date = new Date();
        Long voted = ((long) 1);
        try {
            Vote newVote = new Vote();
            Users help_Voter = Users.findById(Long.parseLong(session().get("userid")));
            Post help_Poster = Post.findById(id);
            Integer checksum = Vote.find.where().eq("post_id", id).eq("vote", 1).findRowCount();
            checksum +=1;
            Vote uniquevote = Vote.find.where().eq("post_id", id).eq("voter_id", session().get("userid")).findUnique();
            if (uniquevote == null) {
                newVote.setId(help_Poster.getId());
                newVote.setVoter(Long.parseLong(session().get("userid")));
                newVote.setPosterId(help_Poster.getPoster());
                newVote.setVoter_name(help_Voter.getVorname() + " " + help_Voter.getNachname());
                newVote.setTimestamp(date);
                newVote.setVote(voted);
                newVote.setVotesum((long) checksum);
                newVote.save();

                Post newHelpPost = Post.findById(id);
                newHelpPost.setVote((long) checksum);
                newHelpPost.save();
            } else {
                return badRequest(login.render(true, "DOPPELTES VOTE!"));
            }

        } catch (Exception e) {
            Logger.error("Error", e);
            return badRequest(network.render(session().get("email"), true, "Datenbankfehler", null));
        }

        return redirect(routes.Network.index());
    }

    /**
     * delete vote
     *
     * @param id
     * @return
     */
    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result delvote(Long id) {

        try {
            Vote helpvote = Vote.find.where().eq("post_id", id).eq("voter_id", session().get("userid")).findUnique();
            if (helpvote == null) {
                return badRequest(login.render(true, "NICHT DEIN VOTE!"));
            } else {
                helpvote.delete();
            }

        } catch (Exception e) {
            Logger.error("Error", e);
            return badRequest(network.render(session().get("email"), true, "Vote existiert nicht", null));
        }
        return redirect(routes.Network.index());

    }
}
