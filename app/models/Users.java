/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * User Database Model
 */

package models;

import play.db.ebean.*;
import play.data.validation.*;
import java.util.*;
import javax.persistence.*;

@Entity
public class Users extends Model {

    @Id
    public Long id;

    public String vorname;

    public String nachname;

    public String email;

    public String wohnort;

    public String birth;

    public String sex;

    public String ip;

    public String registration;

    public String lastlogin;

    public String lastsessionstart;

    public Integer active;

    public Integer blocked;

    public String homepage;

    public String profilbild;

    public String beschreibung;

    public String comment;

    public String pwsafe;

    public String sessionid;

    public static Finder<Long,Users> find = new Finder<Long,Users>(
            Long.class, Users.class
    );

    public static Users findByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }

    public static Users findById(String id) {
        return find.where().eq("id", id).findUnique();
    }
}
