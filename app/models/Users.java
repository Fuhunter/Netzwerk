package models;

import play.db.ebean.*;
import play.data.validation.*;
import java.util.*;
import javax.persistence.*;

@Entity
public class Users extends Model {

    @Id
    public Long id;

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
}