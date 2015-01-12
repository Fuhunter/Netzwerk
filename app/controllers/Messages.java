/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * Network Controller is index for logged in Users
 */

package controllers;

import models.Friendship;
import models.Groupmembers;
import models.Users;
import org.joda.time.DateTime;
import play.Logger;
import play.data.DynamicForm;
import play.mvc.*;
import views.html.*;
import models.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Messages extends Controller {

    /**
     * If authenticated render Indexpage
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result index() {

        List<String> absenderV = new ArrayList<>();
        List<String> absenderN = new ArrayList<>();
        List<String> betreff = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        List<Boolean> readed = new ArrayList<>();
        List<Date> datum = new ArrayList<>();

        for (Message ab: Message.find.select("aid").select("betreff").select("id").select("readed").select("datum").where().eq("eid", session().get("userid")).orderBy("datum").findList()) {
            String abs = Users.findById(ab.getAid()).getVorname();
            String abs2 = Users.findById(ab.getAid()).getNachname();
            absenderV.add(abs);
            absenderN.add(abs2);
            betreff.add(ab.getBetreff());
            ids.add(ab.getId());
            readed.add(ab.getReaded());
            datum.add(ab.getDatum());
        }

        return ok(message.render(session().get("email"), absenderV, absenderN, betreff, ids, readed, datum));
    }

    /**
     * Show page for new message
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result newm() {
        return ok(nmessage.render(session().get("email"), false, ""));
    }

    /**
     * Create new Message
     * @return
     */
    public static Result create() {
        DynamicForm nm = new DynamicForm().bindFromRequest();

        String empfaenger = nm.get("empfaenger");
        String betreff = nm.get("betreff");
        String nachricht = nm.get("nachricht");

        // Check for multiple
        if (empfaenger.contains(",")) {
            String[] mempfaenger = empfaenger.split(",");

            for (String empf: mempfaenger) {
                Message nn = new Message();
                nn.setBetreff(betreff);
                nn.setNachricht(nachricht);
                nn.setAid(Long.parseLong(session().get("userid")));
                nn.setDatum(new Date());
                nn.setReaded(false);

                Long em = Users.findByEmail(empf).getId();

                if (em != null && !em.equals("")) {
                    Friendship ufriends = Friendship.find.where().eq("user_id", session().get("userid")).eq("friend_id", em).findUnique();
                    List<Groupmembers> groups = Groupmembers.find.where().eq("user_id", session().get("userid")).findList();
                    List<Groupmembers> groupsem = Groupmembers.find.where().eq("user_id", em).findList();
                    List<Long> groupids = new ArrayList<>();
                    List<Long> groupemids = new ArrayList<>();

                    for (Groupmembers g: groups) {
                        groupids.add(g.getGroup());
                    }

                    for (Groupmembers g: groupsem) {
                        groupemids.add(g.getGroup());
                    }

                    if (ufriends != null || groupids.contains(groupemids)) {
                        nn.setEid(em);
                        nn.save();
                    } else {
                        Logger.error("Keine Freunde!");
                        return redirect(routes.Messages.index());
                    }
                } else {
                    Logger.error("Empfänger ", empf, "nicht gefunden");
                }
            }
        } else {
            Message nn = new Message();
            nn.setBetreff(betreff);
            nn.setNachricht(nachricht);
            nn.setAid(Long.parseLong(session().get("userid")));
            nn.setDatum(new Date());
            nn.setReaded(false);

            Long em = Users.findByEmail(empfaenger).getId();

            if (em != null && !em.equals("")) {
                Friendship ufriends = Friendship.find.where().eq("user_id", session().get("userid")).eq("friend_id", em).findUnique();
                List<Groupmembers> groups = Groupmembers.find.where().eq("user_id", session().get("userid")).findList();
                List<Groupmembers> groupsem = Groupmembers.find.where().eq("user_id", em).findList();
                List<Long> groupids = new ArrayList<>();
                List<Long> groupemids = new ArrayList<>();

                for (Groupmembers g: groups) {
                    groupids.add(g.getGroup());
                }

                for (Groupmembers g: groupsem) {
                    groupemids.add(g.getGroup());
                }

                if (ufriends != null || groupids.contains(groupemids)) {
                    nn.setEid(em);
                    nn.save();
                }
            } else {
                Logger.error("Empfänger ", empfaenger, "nicht gefunden");
            }
        }

        return redirect(routes.Messages.newm());
    }

    /**
     * Show message
     * @param id
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result show(Long id) {
        Message message = Message.find.byId(id);

        if (message.getReaded() == false) {
            message.setReaded(true);
            message.save();
        }
        return ok(showm.render(session().get("email"), message));
    }

    /**
     * delete message
     * @param id
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result del(Long id) {
        Message message = Message.find.byId(id);

        if (message != null) {
            message.delete();
        }

        return redirect(routes.Messages.index());
    }
}
