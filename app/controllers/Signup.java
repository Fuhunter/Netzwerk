/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * Signup Controller handels login and registration of Users
 */

package controllers;

import assets.BCrypt;
import com.avaje.ebean.Ebean;
import models.Users;
import play.*;
import play.data.*;
import play.data.Form.*;
import play.data.DynamicForm;
import play.db.ebean.Transactional;
import play.mvc.*;
import views.html.*;

public class Signup extends Controller {

    /**
     * Show index Page of App
     * @return Result
     */
    public static Result index() {
        return ok(signup.render(null, null));
    }

    /**
     * Registers new User and insert into Database. Is transactional Action
     * @return
     */
    @Transactional
    public static Result register() {

        // Bind DynamicForm from Request
        DynamicForm newUserForm = new DynamicForm().bindFromRequest();
        String vorname = newUserForm.get("vorname");
        String nachname = newUserForm.get("nachname");
        String email = newUserForm.get("email");
        String geschlecht = newUserForm.get("geschlecht");
        String password = newUserForm.get("password");
        String password2 = newUserForm.get("rpassword");
        String day = newUserForm.get("tt");
        String month = newUserForm.get("mm");
        String year = newUserForm.get("yyyy");

        // Check for empty field
        if (vorname.isEmpty() == true || vorname == "") {
            return  badRequest(signup.render(true, "Vorname darf nicht leer sein"));

        } else if (nachname.isEmpty() == true || nachname == "") {
            return  badRequest(signup.render(true, "Nachname darf nicht leer sein"));

        } else if (email.isEmpty() == true || email == "") {
            return  badRequest(signup.render(true, "Email darf nicht leer sein"));

        } else if (password.isEmpty() == true || password == "") {
            return  badRequest(signup.render(true, "Passwort darf nicht leer sein"));

        } else if (password2.isEmpty() == true || password2 == "") {
            return  badRequest(signup.render(true, "Passwort darf nicht leer sein"));
        }

        // Hash Pass with BCrypt and check if user already exists
        String hashpass = BCrypt.hashpw(password, BCrypt.gensalt(12));
        boolean matched = BCrypt.checkpw(password2, hashpass);

        if (matched != true) {
            return badRequest(signup.render(true, "Passwort nicht identisch!"));
        }

        Users checkUser = Users.findByEmail(email);

        if (checkUser != null) {
            return badRequest(signup.render(true, "E-Mail Adresse bereits vorhanden"));
        }

        // Insert into Database
        try {
            Users newUser = new Users();
            newUser.birth = day + "." + month + "." + year;
            newUser.email = email;
            newUser.vorname = vorname;
            newUser.nachname = nachname;
            newUser.sex = geschlecht;
            newUser.pwsafe = hashpass;
            newUser.active = 1;
            newUser.blocked = 0;
            newUser.save();

            /*
            // Load SMTP configuration
            String smtpHost = Play.application().configuration().getString( "smtp.host" );
            Integer smtpPort = Play.application().configuration().getInt( "smtp.port );
            String smtpUser = Play.application().configuration().getString( "smtp.user" );
            String smtpPassword = Play.application().configuration().getString( "smtp.password );

            // Render template
            String body = email.render( created ).body();

            // Prepare email
            Email mail = new SimpleEmail();
            mail.setFrom( "y...@host.com" );
            mail.setSubject( subject );
            mail.setMsg( body );
            mail.addTo( to );

            // Application de la configuration SMTP
            mail.setHostName( smtpHost );
            if ( smtpPort != null && smtpPort > 1 && smtpPort < 65536 ) {
                mail.setSmtpPort( smtpPort );
            }
            if ( ! StringUtils.isEmpty(smtpUser) ) {
                mail.setAuthentication( smtpUser, smtpPassword );
            }

            // And finally
            mail.send();
            newUser.active = 0;
             */
        }
        catch (Exception e) {
            return badRequest(signup.render(true, "Datenbank Fehler"));
        }

        return ok(signup.render(false, "Success"));
    }

    /**
     * Logins a User by typed Email and Password
     * @return
     */
    @Transactional
    public static Result login() {

        // Bind DynamicForm from Request
        DynamicForm loginForm = new DynamicForm().bindFromRequest();
        String password = loginForm.get("password");
        String email = loginForm.get("email");

        // Search for User by Email in Database
        // If Found login, else redirect with error
        Users user = Users.findByEmail(email);

        if (user == null) {
            return badRequest(login.render(true, "Email Adresse falsch!"));
        }
        else if (BCrypt.checkpw(password,user.pwsafe) == false) {
            return badRequest(login.render(true, "Falsches Passwort!"));
        }
        else {
            session().clear();

            String id = session("id");
            if(id == null) {
                id = java.util.UUID.randomUUID().toString();
                session("id", id);
            }

            user.sessionid = session().get("id");
            user.save();
            session("email", email);
            session("userid", Long.toString(user.id));

            return redirect(routes.Network.index());
        }
    }

    /**
     * Shows Loginpage
     * @return
     */
    public static Result loginPage() {
        String message = null;
        if (message == null || message.equals("")) {
            return ok(login.render(false, null));
        } else {
            return badRequest(login.render(true, message));
        }
    }

    /**
     * Destroys session -> User is logged out
     * @return
     */
    @Transactional
    public static Result logout() {
        Users user = Users.findByEmail(session().get("email"));
        user.sessionid = null;
        user.save();
        session().clear();
        return redirect(routes.Application.index());
    }
}