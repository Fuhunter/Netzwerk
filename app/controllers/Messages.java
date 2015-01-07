/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * Network Controller is index for logged in Users
 */

package controllers;

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

        for (Message ab: Message.find.select("aid").select("betreff").select("id").where().eq("eid", session().get("userid")).findList()) {
            String abs = Users.findById(ab.getAid()).getVorname();
            String abs2 = Users.findById(ab.getAid()).getNachname();
            absenderV.add(abs);
            absenderN.add(abs2);
            betreff.add(ab.getBetreff());
            ids.add(ab.getId());
        }

        return ok(message.render(session().get("email"), absenderV, absenderN, betreff, ids));
    }

    @Security.Authenticated(Secured.class)
    public static Result newm() {
        return ok(nmessage.render(session().get("email"), false, ""));
    }

    public static Result create() {
        DynamicForm nm = new DynamicForm().bindFromRequest();

        String empfaenger = nm.get("empfaenger");
        String betreff = nm.get("betreff");
        String nachricht = nm.get("nachricht");

        if (empfaenger.contains(",")) {
            String[] mempfaenger = empfaenger.split(",");

            for (String empf: mempfaenger) {
                Message nn = new Message();
                nn.setBetreff(betreff);
                nn.setNachricht(nachricht);
                nn.setAid(Long.parseLong(session().get("userid")));
                nn.setDatum(new Date());

                Long em = Users.findByEmail(empf).getId();

                if (em != null && !em.equals("")) {
                    nn.setEid(em);
                    nn.save();
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

            Long em = Users.findByEmail(empfaenger).getId();

            if (em != null && !em.equals("")) {
                nn.setEid(em);
                nn.save();
            } else {
                Logger.error("Empfänger ", empfaenger, "nicht gefunden");
            }
        }

        return redirect(routes.Messages.newm());
    }

    @Security.Authenticated(Secured.class)
    public static Result show(Long id) {
        Message message = Message.find.byId(id);
        return ok(showm.render(session().get("email"), message));
    }
}
